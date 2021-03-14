package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.admin.repository.impl.SettingDAO;
import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.validation.Length;
import org.avalon.rainbow.common.validation.NotBlank;
import org.avalon.rainbow.common.validation.ValidateWithMethod;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_SETTING")
public class Setting extends BasePO {

    @NotBlank(errorCode = "err.mandatory")
    @Length(max = 64)
    @ValidateWithMethod(methodName = "validateKey", message = "Setting already exists in DB")
    private String settingKey;

    @NotBlank(errorCode = "err.mandatory")
    @Length(max = 64)
    private String value;

    @Length(max = 16)
    private String type;

    @Length(max = 500)
    private String description;


    private boolean validateKey(String key) {
        SettingDAO settingDAO = BeanUtils.getBean(SettingDAO.class);
        return settingDAO.findBySettingKey(key) == null;
    }

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
