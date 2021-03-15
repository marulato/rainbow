package org.avalon.rainbow.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.avalon.rainbow.admin.entity.User;
import org.avalon.rainbow.admin.entity.UserRole;
import org.avalon.rainbow.common.constant.AppConst;

import java.util.List;

public class LoginResult {

    private String message;
    private String fieldName;
    private String userStatus;
    private boolean success;
    @JsonIgnore
    private boolean pwdCorrect;
    @JsonIgnore
    private User user;
    private List<UserRole> roles;

    public LoginResult() {}

    public static LoginResult incorrectPassword(User user) {
        LoginResult result = new LoginResult();
        result.setMessage("User doesn't exist or password is incorrect");
        result.setFieldName("password");
        result.setSuccess(false);
        result.setUser(user);
        result.setPwdCorrect(false);
        return result;
    }

    public static LoginResult status(String status, User user) {
        LoginResult result = new LoginResult();
        result.setUser(user);
        result.setSuccess(false);
        result.setPwdCorrect(true);
        if (AppConst.ACCOUNT_STATUS_ACTIVE.equals(status)) {
            result.setSuccess(true);
            result.setMessage("Login successfully");
        } else if (AppConst.ACCOUNT_STATUS_EXPIRED.equals(status)) {
            result.setMessage("Account Expired");
            result.setFieldName("password");
        } else if (AppConst.ACCOUNT_STATUS_LOCKED.equals(status)) {
            result.setMessage("Account Locked");
            result.setFieldName("password");
        } else if (AppConst.ACCOUNT_STATUS_CANCELLED.equals(status)) {
            result.setMessage("Account Cancelled");
            result.setFieldName("password");
        } else if (AppConst.ACCOUNT_STATUS_INACTIVE.equals(status)) {
            result.setMessage("Account Inactive");
            result.setFieldName("password");
        }
        return result;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPwdCorrect() {
        return pwdCorrect;
    }

    public void setPwdCorrect(boolean pwdCorrect) {
        this.pwdCorrect = pwdCorrect;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }
}
