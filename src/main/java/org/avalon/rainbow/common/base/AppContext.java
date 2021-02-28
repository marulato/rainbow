package org.avalon.rainbow.common.base;

import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class AppContext implements Serializable {

    private String userId;
    private String domain;
    private String name;
    private boolean isAdminRole;
    private boolean loggedIn;
    private String sessionId;

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
        context.setUserId(StringUtils.isNotBlank(virtualId) ? virtualId : "PublicIndex");
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
