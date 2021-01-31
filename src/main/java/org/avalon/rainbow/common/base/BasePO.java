package org.avalon.rainbow.common.base;

import java.io.Serializable;
import java.util.Date;


public abstract class BasePO implements Serializable {

    private Date createdAt;
    private String createdBy;
    private String createdDm;
    private Date updatedAt;
    private String updatedBy;
    private String updatedDm;

    public final void createAuditValues(AppContext context) {
        if (context != null) {
            Date now = new Date();
            setCreatedBy(context.getUserId());
            setUpdatedBy(context.getUserId());
            setCreatedAt(now);
            setUpdatedAt(now);
        }
    }

    public final void updateAuditValues(AppContext context) {
        if (context != null) {
            Date now = new Date();
            setUpdatedBy(context.getUserId());
            setUpdatedAt(now);
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedDm() {
        return createdDm;
    }

    public void setCreatedDm(String createdDm) {
        this.createdDm = createdDm;
    }

    public String getUpdatedDm() {
        return updatedDm;
    }

    public void setUpdatedDm(String updatedDm) {
        this.updatedDm = updatedDm;
    }
}
