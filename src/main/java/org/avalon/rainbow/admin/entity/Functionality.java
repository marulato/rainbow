package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.validation.MasterCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_FUNCTION")
public class Functionality extends BasePO {

    private Integer moduleId;
    private String code;
    private String name;
    private String description;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
