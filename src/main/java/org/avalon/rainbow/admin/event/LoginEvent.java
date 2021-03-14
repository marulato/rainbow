package org.avalon.rainbow.admin.event;

import org.avalon.rainbow.admin.entity.User;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {

    private String loginAction;
    private final User user;

    public LoginEvent(Object source, User user, String loginAction) {
        super(source);
        this.loginAction = loginAction;
        this.user = user;
    }

    public String getLoginAction() {
        return loginAction;
    }

    public void setLoginAction(String loginAction) {
        this.loginAction = loginAction;
    }

    public User getUser() {
        return user;
    }
}
