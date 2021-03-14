package org.avalon.rainbow.admin.entity;

import org.avalon.rainbow.common.base.BasePO;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USR_LOGIN_TRANSACTION")
public class LoginTransaction extends BasePO {

    private Long userId;
    private String username;
    private String status;
    private String loginStatus;
    private String ip;
    private String userAgent;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
