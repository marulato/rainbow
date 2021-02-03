package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_MASTER_CODE")
public class MasterCode extends BasePO {

    @NotNull(errorCode = "cm.mandatory")
    private String codeType;
    private String code;
    private String value;
    private String description;
    private String remarks;
    private String isConfigurable;
    private Integer displayOrder;

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsConfigurable() {
        return isConfigurable;
    }

    public void setIsConfigurable(String isConfigurable) {
        this.isConfigurable = isConfigurable;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
