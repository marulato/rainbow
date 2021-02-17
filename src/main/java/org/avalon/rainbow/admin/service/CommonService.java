package org.avalon.rainbow.admin.service;

import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.admin.entity.Setting;
import org.avalon.rainbow.admin.repository.impl.MessageDAO;
import org.avalon.rainbow.common.cache.MessageCache;
import org.avalon.rainbow.common.cache.PropertiesCache;
import org.avalon.rainbow.common.cache.SettingCache;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;

@Service
public class CommonService {

    private final MessageDAO messageDAO;

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
}
