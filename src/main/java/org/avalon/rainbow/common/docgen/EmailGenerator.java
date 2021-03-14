package org.avalon.rainbow.common.docgen;

import org.avalon.rainbow.admin.entity.EmailObject;
import org.avalon.rainbow.admin.entity.Template;
import org.avalon.rainbow.admin.service.TemplateService;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.StringUtils;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class EmailGenerator extends PdfGenerator {

    private final TemplateService templateService = BeanUtils.getBean(TemplateService.class);


    public EmailObject generateEmail(String refTable, Long refId) throws Exception {
        Template template = templateService.getTemplateByPath(getTemplate());
        if (template == null) {
            throw new FileNotFoundException();
        }
        EmailObject emailObject = new EmailObject();
        String modifiedTemplatePath = templateService.getActualTemplatePath(getTemplate());
        byte[] data = generate(modifiedTemplatePath, getParameters());
        emailObject.setSubject(getSubject());
        emailObject.setRecipients(getRecipients(template.getRecipient()));
        emailObject.setCcs(getRecipients(template.getCc()));
        emailObject.setContent(new String(data, StandardCharsets.UTF_8));
        emailObject.setAttachments(getAttachments());
        emailObject.setReferenceTbl(refTable);
        emailObject.setReferenceId(refId);
        emailObject.setActive(StringUtils.parseBoolean(template.getIsActive()));
        return emailObject;
    }

    private List<String> getRecipients(String recipientString) {
        String[] var = recipientString.trim().split(",");
        List<String> list = new ArrayList<>();
        for (String s : var) {
            list.add(s.trim().toLowerCase());
        }
        return list;
    }

    protected abstract Map<String, byte[]> getAttachments();

    protected abstract String getSubject();

}
