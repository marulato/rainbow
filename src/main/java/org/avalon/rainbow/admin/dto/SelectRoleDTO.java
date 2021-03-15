package org.avalon.rainbow.admin.dto;

import org.avalon.rainbow.admin.entity.RoleAssignment;
import org.avalon.rainbow.admin.entity.User;
import org.avalon.rainbow.admin.entity.UserRole;
import org.avalon.rainbow.admin.repository.impl.RoleAssignmentDAO;
import org.avalon.rainbow.admin.repository.impl.UserDAO;
import org.avalon.rainbow.admin.repository.impl.UserRoleDAO;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.validation.NotNull;
import org.avalon.rainbow.common.validation.ValidateWithMethod;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SelectRoleDTO implements Serializable {

    //@ValidateWithMethod(methodName = "validateToken", message = "Invalid token")
    private String token;

    @NotNull
    @ValidateWithMethod(methodName = "validateUserId", message = "Invalid user")
    private Long userId;

    @NotNull
    @ValidateWithMethod(methodName = "validateRole", message = "Invalid role selected")
    private String roleCode;

/*    public boolean validateToken(String token) throws Exception {
        String sessionToken = (String) SessionManager.getAttribute(AccessConst.LOGIN_TOKEN);
        if (sessionToken == null || !sessionToken.equals(token)) {
            return false;
        }
        LoginToken loginToken = new ObjectMapper().readValue(EncryptionUtils.decryptToken(token), LoginToken.class);
        User user = BeanUtils.getBean(UserDAO.class).findById(loginToken.getId());
        return DateUtils.getMinutesBetween(loginToken.getDate(), new Date()) < 5 && user != null
                && user.getUsername().equals(loginToken.getName());
    }*/

    public boolean validateUserId(Long userId) {
        User user = BeanUtils.getBean(UserDAO.class).findById(userId);
        return user != null;
    }

    public boolean validateRole(String roleCode) throws Exception {
        //LoginToken loginToken = new ObjectMapper().readValue(EncryptionUtils.decryptToken(token), LoginToken.class);
        User user = BeanUtils.getBean(UserDAO.class).findById(userId);
        Date now = new Date();

        List<RoleAssignment> roleAssignmentList = BeanUtils.getBean(RoleAssignmentDAO.class)
                .findByUserIdAndStatus(user.getId(), AppConst.ACCOUNT_STATUS_ACTIVE);
        roleAssignmentList.removeIf(v -> v.getEffectiveDate().after(now) || v.getExpirationDate().before(now));
        for (RoleAssignment roleAssignment : roleAssignmentList) {
            UserRole userRole = BeanUtils.getBean(UserRoleDAO.class).findById(roleAssignment.getRoleId());
            if (userRole.getCode().equals(roleCode)) {
                return true;
            }
        }
        return false;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
