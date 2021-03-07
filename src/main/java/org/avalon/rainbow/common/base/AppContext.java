package org.avalon.rainbow.common.base;

import org.avalon.rainbow.admin.entity.RoleAccess;
import org.avalon.rainbow.admin.entity.RoleAssignment;
import org.avalon.rainbow.admin.entity.UserRole;
import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AppContext implements Serializable {

    private Integer userId;
    private String username;
    private String displayName;
    private String domain;
    private boolean isAdminRole;
    private boolean loggedIn;
    private String sessionId;
    private UserRole currentRole;
    private List<RoleAccess> accesses;
    private List<RoleAssignment> roleAssignments;
    private List<UserRole> allRoles;

    public static final String APP_CONTEXT_KEY = "Legion_Web_Session_Context";
    private static final ThreadLocal<AppContext> localContext = new ThreadLocal<>();

    public static AppContext getFromWebThread() {

        AppContext context = null;
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes();
            context = (AppContext) requestAttributes.getRequest().getSession().getAttribute(APP_CONTEXT_KEY);
        } catch (Exception e) {
            context = getLocalAppContext();
        }
        return context;
    }

    public static AppContext getAppContext(HttpServletRequest request) {
        if (request != null) {
            Object obj = request.getSession().getAttribute(APP_CONTEXT_KEY);
            if (obj != null) {
                return (AppContext) obj;
            }
        }
        return null;
    }

    public static AppContext createVirtualContext(String virtualId, boolean isLoggedIn, HttpServletRequest request) {
        AppContext context = new AppContext();
        context.setUsername(StringUtils.isNotBlank(virtualId) ? virtualId : "PublicIndex");
        context.setDomain("internet");
        context.setSessionId(request.getSession().getId());
        context.setLoggedIn(isLoggedIn);
        request.getSession().setAttribute(APP_CONTEXT_KEY, context);
        return context;
    }

    public void setAppContext() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        requestAttributes.getRequest().getSession().setAttribute(APP_CONTEXT_KEY, this);
    }

    public void setAppContext(HttpServletRequest request) {
        if (request != null) {
            request.getSession().setAttribute(APP_CONTEXT_KEY, this);
        }
    }

    public void createLocalAppContext() {
        localContext.set(this);
    }

    public static AppContext getLocalAppContext() {
        return localContext.get();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isAdminRole() {
        return isAdminRole;
    }

    public void setAdminRole(boolean adminRole) {
        isAdminRole = adminRole;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<RoleAccess> getAccesses() {
        return accesses;
    }

    public void setAccesses(List<RoleAccess> accesses) {
        this.accesses = accesses;
    }

    public List<RoleAssignment> getRoleAssignments() {
        return roleAssignments;
    }

    public void setRoleAssignments(List<RoleAssignment> roleAssignments) {
        this.roleAssignments = roleAssignments;
    }

    public UserRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(UserRole currentRole) {
        this.currentRole = currentRole;
    }

    public List<UserRole> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<UserRole> allRoles) {
        this.allRoles = allRoles;
    }
}
