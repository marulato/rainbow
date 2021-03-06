package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CM_MESSAGE")
public class Message extends BasePO {

    private String msgKey;
    private String type;
    private String msgValue;
    private String dftValue;
    private String remarks;

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsgValue() {
        return msgValue;
    }

    public void setMsgValue(String msgValue) {
        this.msgValue = msgValue;
    }

    public String getDftValue() {
        return dftValue;
    }

    public void setDftValue(String dftValue) {
        this.dftValue = dftValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
