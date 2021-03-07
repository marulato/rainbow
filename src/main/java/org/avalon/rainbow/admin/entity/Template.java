package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.utils.StringUtils;
import org.avalon.rainbow.common.utils.ValidationUtils;
import org.avalon.rainbow.common.validation.*;
import org.avalon.rainbow.common.validation.MasterCode;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_TEMPLATE")
public class Template extends BasePO {

    @NotEmpty(errorCode = "err.mandatory")
    @MatchesPattern(pattern = "[A-Za-z0-9_]{4,50}", errorCode = "err.invalid")
    private String name;

    @NotBlank(errorCode = "err.mandatory")
    @MasterCode(type = "cm.template.type", errorCode = "err.invalid")
    private String type;
    private String category;
    private String path;
    @Length(errorCode = "err.maxlength")
    private String modifiedContent;
    @Length(max = 1000, errorCode = "err.maxlength")
    private String remarks;

    @Length(max = 2000, errorCode = "err.maxlength")
    @ValidateWithMethod.Group({
            @ValidateWithMethod(methodName = "validateRecipientNotNull", errorCode = "err.mandatory"),
            @ValidateWithMethod(methodName = "validateRecipient", errorCode = "err.invalid")
    })
    private String recipient;

    @Length(max = 2000, errorCode = "err.maxlength")
    @ValidateWithMethod(methodName = "validateRecipient", errorCode = "err.invalid")
    private String cc;

    @Length(max = 2000, errorCode = "err.maxlength")
    @ValidateWithMethod(methodName = "validateRecipient", errorCode = "err.invalid")
    private String bcc;

    @ValidateWithMethod.Group({
            @ValidateWithMethod(methodName = "validateMobileNoNotNull", errorCode = "err.mandatory"),
            @ValidateWithMethod(methodName = "validateMobileNo", errorCode = "err.invalid")
    })
    private String mobileNo;

    @NotBlank(errorCode = "err.mandatory")
    @MasterCode(type = "cm.boolean", errorCode = "err.invalid")
    private String isEditable;

    @NotBlank(errorCode = "err.mandatory")
    @MasterCode(type = "cm.boolean", errorCode = "err.invalid")
    private String isActive;


    private boolean validateRecipientNotNull(String recipient) {
        if (AppConst.TEMPLATE_TYPE_EMAIL.equals(this.type)) {
            return StringUtils.isNotBlank(recipient);
        }
        return true;
    }

    private boolean validateRecipient(String recipient) {
        if (AppConst.TEMPLATE_TYPE_EMAIL.equals(this.type) && StringUtils.isNotBlank(recipient)) {
            String[] recipients = recipient.split(",");
            for (String s : recipients) {
                if (!ValidationUtils.isValidEmail(s.trim())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateMobileNoNotNull(String mobileNo) {
        if (AppConst.TEMPLATE_TYPE_SMS.equals(this.type)) {
            return StringUtils.isNotBlank(recipient);
        }
        return true;
    }

    private boolean validateMobileNo(String mobileNo) {
        if (AppConst.TEMPLATE_TYPE_SMS.equals(this.type) && StringUtils.isNotBlank(mobileNo)) {
            String[] mobileNos = mobileNo.split(",");
            for (String s : mobileNos) {
                if (!s.trim().matches("^1[0-9]{10}")) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModifiedContent() {
        return modifiedContent;
    }

    public void setModifiedContent(String modifiedContent) {
        this.modifiedContent = modifiedContent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(String isEditable) {
        this.isEditable = isEditable;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
