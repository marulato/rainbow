package org.avalon.rainbow.admin.controller;

import org.avalon.rainbow.admin.dto.LoginAccountDTO;
import org.avalon.rainbow.admin.dto.SelectRoleDTO;
import org.avalon.rainbow.admin.service.UserAccountService;
import org.avalon.rainbow.common.aop.permission.RequiresRoles;
import org.avalon.rainbow.common.base.AccessConst;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.base.RestResponse;
import org.avalon.rainbow.common.base.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
public class IntranetIndexController {

    private final UserAccountService userAccountService;


    @Autowired
    public IntranetIndexController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(LoginAccountDTO dto, HttpServletRequest request) throws Exception {
        RestResponse restResponse = userAccountService.login(dto);
        AppContext appContext = AppContext.getAppContext(request);
        if (appContext != null) {
            appContext.invalidate(request);
        }
        return ResponseEntity.ok(restResponse.getResult());
    }

    @PostMapping("/login/selectRole")
    public RestResponse selectRole(SelectRoleDTO selectRoleDTO, HttpServletRequest request) throws Exception {
        return userAccountService.selectRole(selectRoleDTO, request);
    }

    @RequiresRoles(AccessConst.ROLE_SUPER_ADMIN)
    @GetMapping("/test")
    public Object test() {
        return "ok";
    }
}
