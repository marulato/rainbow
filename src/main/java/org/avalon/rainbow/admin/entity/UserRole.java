package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USR_ROLE")
public class UserRole extends BasePO {

    private String code;
    private String name;
    private String description;
    private String isSystem;

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

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
}
