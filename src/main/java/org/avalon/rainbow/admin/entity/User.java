package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.utils.ValidationUtils;
import org.avalon.rainbow.common.validation.ValidateWithMethod;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "USR_USER")
public class User extends BasePO {

    @ValidateWithMethod(methodName = "validateUsername", errorCode = "err.invalid")
    private String username;
    private String password;
    private String domain;
    private String displayName;
    private String status;
    private Date effectiveDate;
    private Date expirationDate;
    private String isFirstLogin;
    private Integer loginFailedTimes;
    private Date lastLoginSuccessDt;
    private Date lastLoginAttemptDt;

    private boolean validateUsername(String username) {
        return ValidationUtils.isValidUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public Integer getLoginFailedTimes() {
        return loginFailedTimes;
    }

    public void setLoginFailedTimes(Integer loginFailedTimes) {
        this.loginFailedTimes = loginFailedTimes;
    }

    public Date getLastLoginSuccessDt() {
        return lastLoginSuccessDt;
    }

    public void setLastLoginSuccessDt(Date lastLoginSuccessDt) {
        this.lastLoginSuccessDt = lastLoginSuccessDt;
    }

    public Date getLastLoginAttemptDt() {
        return lastLoginAttemptDt;
    }

    public void setLastLoginAttemptDt(Date lastLoginAttemptDt) {
        this.lastLoginAttemptDt = lastLoginAttemptDt;
    }
}
