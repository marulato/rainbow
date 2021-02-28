package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.utils.StringUtils;
import org.avalon.rainbow.common.utils.ValidationUtils;
import org.avalon.rainbow.common.validation.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EmailObject implements Serializable {

    @NotBlank(errorCode = "cm.mandatory")
    @Length(max = 160)
    private String subject;

    @ValidateWithMethod(methodName = "validateRecipients")
    private List<String> recipients;

    @ValidateWithMethod(methodName = "validateCcs")
    private List<String> ccs;

    @NotEmpty(errorCode = "cm.mandatory")
    @ValidateWithMethod(methodName = "validateContent")
    private String content;

    @ValidateWithMethod(methodName = "validateAttachments")
    private Map<String, byte[]> attachments;

    private String referenceTbl;
    private Long referenceId;
    private String triggeredBy;

    public EmailObject() {
        attachments = new HashMap<>();
        recipients = new ArrayList<>();
        ccs = new ArrayList<>();
    }

    public void addAttachment(String fileName, byte[] data) {
        if (attachments != null && StringUtils.isEmpty(fileName) && data != null) {
            attachments.put(fileName, data);
        }
    }

    public void addRecipients(String... recipient) {
        if (recipients != null && recipient != null) {
            for (String email : recipient) {
                if (!recipients.contains(email) && StringUtils.isNotBlank(email)) {
                    recipients.add(email);
                }
            }
        }
    }

    public void addCCs(String... cc) {
        if (ccs != null && cc != null) {
            for (String email : cc) {
                if (!ccs.contains(email) && StringUtils.isNotBlank(email)) {
                    ccs.add(email);
                }
            }
        }
    }

    private boolean validateRecipients(List<String> emails) {
        if (emails != null) {
            for (String email : emails) {
                if (!ValidationUtils.isValidEmail(email)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean validateCcs(List<String> emails) {
        if (emails != null) {
            for (String email : emails) {
                if (!ValidationUtils.isValidEmail(email)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateContent(String content) {
        byte[] data = content.getBytes(StandardCharsets.UTF_8);
        return data.length <= 16 * 1024 * 1024;
    }

    private boolean validateAttachments(Map<String, byte[]> map) {
        if (map != null) {
            Set<String> fileNames = map.keySet();
            for (String fileName : fileNames) {
                byte[] data = map.get(fileName);
                if (StringUtils.isBlank(fileName) || fileName.length() > 120
                        || data == null || data.length == 0 || data.length > 100 * 1024 * 1024) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCcs() {
        return ccs;
    }

    public void setCcs(List<String> ccs) {
        this.ccs = ccs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, byte[]> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, byte[]> attachments) {
        this.attachments = attachments;
    }

    public String getReferenceTbl() {
        return referenceTbl;
    }

    public void setReferenceTbl(String referenceTbl) {
        this.referenceTbl = referenceTbl;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}
