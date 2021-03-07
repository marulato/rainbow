package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_MODULE")
public class Module extends BasePO {

    private String code;
    private String name;
    private String description;

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
