package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_SETTING")
public class Setting extends BasePO {

    private String settingKey;
    private String value;
    private String type;
    private String description;

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
