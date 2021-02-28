package org.avalon.rainbow.admin.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.avalon.rainbow.common.base.BasePO;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "CM_EMAIL_TRANSACTION")
public class EmailTransaction extends BasePO {

    private String host;
    private String sendFrom;
    private String recipients;
    private String cc;
    private String subject;
    private byte[] content;
    private String attachment;
    private String status;
    private String statusDesc;
    private String triggeredBy;
    private String referenceTbl;
    private Long referenceId;

    public EmailTransaction(EmailObject emailObject) {
        if (emailObject != null) {
            recipients = appendRecipient(emailObject.getRecipients());
            cc = appendRecipient(emailObject.getCcs());
            subject = emailObject.getSubject();
            content = emailObject.getContent().getBytes(StandardCharsets.UTF_8);
            referenceTbl = emailObject.getReferenceTbl();
            referenceId = emailObject.getReferenceId();
            triggeredBy = emailObject.getTriggeredBy();
            try {
                attachment = getAttachment(emailObject.getAttachments());
            } catch (Exception ignored) {

            }
        }
    }

    private String appendRecipient(List<String> recipients) {
        if (recipients != null && !recipients.isEmpty()) {
            StringBuilder to = new StringBuilder();
            for (String recipient : recipients) {
                to.append(recipient).append(",");
            }
            if (to.length() > 0) {
                to.deleteCharAt(to.lastIndexOf(","));
            }
            return to.toString();
        }
        return null;
    }

    private String getAttachment(Map<String, byte[]> map) throws Exception {
        if (map != null && !map.isEmpty()) {
            List<Map<String, String>> attachmentList = new ArrayList<>();
            map.forEach((k, v) -> {
                Map<String, String> json = new HashMap<>();
                json.put("fileName", k);
                json.put("size", String.valueOf(v.length));
                attachmentList.add(json);
            });
            return new ObjectMapper().writeValueAsString(attachmentList);
        }
        return null;
    }

    public EmailTransaction() {}

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
