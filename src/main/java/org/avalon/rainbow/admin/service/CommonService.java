package org.avalon.rainbow.admin.service;

import org.avalon.rainbow.admin.entity.*;
import org.avalon.rainbow.admin.repository.impl.EmailTransactionDAO;
import org.avalon.rainbow.admin.repository.impl.MessageDAO;
import org.avalon.rainbow.admin.repository.impl.SettingDAO;
import org.avalon.rainbow.common.aop.validation.RequiresValidation;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.cache.MasterCodeCache;
import org.avalon.rainbow.common.cache.MessageCache;
import org.avalon.rainbow.common.cache.PropertiesCache;
import org.avalon.rainbow.common.cache.SettingCache;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Service
public class CommonService {

    private final MessageDAO messageDAO;
    private static final Logger log = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    public CommonService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public static String getMessage(String key, Object[] replacements) {
        MessageCache cache = BeanUtils.getBean(MessageCache.class);
        Message message =  cache.get(key);
        if (message != null) {
            if (replacements == null || replacements.length == 0) {
                return message.getMsgValue();
            } else {
                return MessageFormat.format(message.getMsgValue(), replacements);
            }
        }
        return null;
    }

    public static String getMessage(String key) {
        return getMessage(key, null);
    }

    public static String getSetting(String key, String defaultValue) {
        PropertiesCache propertiesCache = BeanUtils.getBean(PropertiesCache.class);
        String value = propertiesCache.get(key);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }
        SettingCache cache = BeanUtils.getBean(SettingCache.class);
        Setting setting = cache.get(key);
        if (setting != null) {
            return setting.getValue() == null ? defaultValue : setting.getValue();
        }
        return defaultValue;
    }

    public static String getSetting(String key) {
        return getSetting(key, null);
    }

    public Message getMessageByKey(String key)  {
        return messageDAO.findByKey(key);
    }

    public List<MasterCode> getMasterCodeByType(String type) {
        MasterCodeCache cache = BeanUtils.getBean(MasterCodeCache.class);
        return cache.get(type);
    }

    @RequiresValidation
    public void sendEmail(EmailObject emailObject) {
        if (emailObject == null) {
            log.warn("Email Object is NULL");
            return;
        }
        if (!isEmailSendingEnabled(emailObject)) {
            log.info("Email Sending is DISABLED");
            return;
        }
        JavaMailSenderImpl mailSender = BeanUtils.getBean(JavaMailSenderImpl.class);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        try {
            messageHelper.setFrom(getSetting("server.email.username"));
            messageHelper.setTo(emailObject.getRecipients().toArray(new String[0]));
            messageHelper.setCc(emailObject.getCcs().toArray(new String[0]));
            messageHelper.setSubject(emailObject.getSubject());
            messageHelper.setText(emailObject.getContent());
            if (emailObject.getAttachments() != null) {
                Set<String> nameSet = emailObject.getAttachments().keySet();
                for (String name : nameSet) {
                    messageHelper.addAttachment(name, new ByteArrayResource(emailObject.getAttachments().get(name)));
                }
            }
            new Thread(new EmailWorkThread(emailObject, mimeMessage, mailSender, AppContext.getFromWebThread())).start();
        } catch (Exception e) {
            log.error("Initialize Email FAILED", e);
        }
    }

    public boolean isEmailSendingEnabled(EmailObject object) {
        return StringUtils.parseBoolean(getSetting("server.email.enabled", "N")) && object.isActive();
    }

    @RequiresValidation
    @Transactional
    public void saveSetting(Setting setting) {
        BeanUtils.getBean(SettingDAO.class).save(setting);
    }


    private static class EmailWorkThread implements Runnable {

        private final MimeMessage mimeMessage;
        private final JavaMailSenderImpl javaMailSender;
        private final EmailObject emailObject;
        private final AppContext context;
        EmailWorkThread(EmailObject emailObject, MimeMessage mimeMessage, JavaMailSenderImpl javaMailSender, AppContext context) {
            this.mimeMessage = mimeMessage;
            this.javaMailSender = javaMailSender;
            this.emailObject = emailObject;
            this.context = context;
        }
        @Override
        public void run() {
            EmailTransaction transaction = new EmailTransaction(emailObject);
            transaction.setHost(getSetting("server.email.host"));
            transaction.setSendFrom(getSetting("server.email.username"));
            try {
                javaMailSender.send(mimeMessage);
                transaction.setStatus(AppConst.EMAIL_STATUS_SENT);
            } catch (Exception e) {
                log.error("Send Email FAILED", e);
                transaction.setStatus(AppConst.EMAIL_STATUS_FAILED);
            } finally {
                BeanUtils.getBean(EmailTransactionDAO.class).save(transaction, context);
            }
        }
    }

}
