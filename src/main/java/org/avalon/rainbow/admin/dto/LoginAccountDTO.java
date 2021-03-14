package org.avalon.rainbow.admin.dto;

import org.avalon.rainbow.admin.entity.User;
import org.avalon.rainbow.admin.repository.impl.UserDAO;
import org.avalon.rainbow.admin.validator.LoginValidator;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.validation.Validate;
import java.io.Serializable;

@Validate(LoginValidator.class)
public class LoginAccountDTO implements Serializable {

    private String username;
    private String password;
    private String userAgent;
    private String ip;

    public User getUser() {
        return BeanUtils.getBean(UserDAO.class).findByUsername(username);
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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
