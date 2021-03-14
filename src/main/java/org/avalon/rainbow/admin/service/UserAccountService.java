package org.avalon.rainbow.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import org.avalon.rainbow.admin.dto.LoginAccountDTO;
import org.avalon.rainbow.admin.dto.LoginToken;
import org.avalon.rainbow.admin.dto.SelectRoleDTO;
import org.avalon.rainbow.admin.entity.*;
import org.avalon.rainbow.admin.event.LoginEvent;
import org.avalon.rainbow.admin.repository.impl.*;
import org.avalon.rainbow.admin.validator.LoginValidator;
import org.avalon.rainbow.common.aop.validation.RequiresValidation;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.base.RestResponse;
import org.avalon.rainbow.common.base.SessionManager;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.EncryptionUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.avalon.rainbow.common.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserAccountService {

    private static final Logger log = LoggerFactory.getLogger(UserAccountService.class);

    private final RoleAssignmentDAO roleAssignmentDAO;
    private final UserRoleDAO userRoleDAO;
    private final UserDAO userDAO;
    private final RoleAccessDAO roleAccessDAO;
    private final ModuleDAO moduleDAO;
    private final FunctionDAO functionDAO;

    @Autowired
    public UserAccountService(
            RoleAssignmentDAO roleAssignmentDAO,
            UserRoleDAO userRoleDAO,
            UserDAO userDAO,
            RoleAccessDAO roleAccessDAO,
            ModuleDAO moduleDAO,
            FunctionDAO functionDAO) {
        this.roleAssignmentDAO = roleAssignmentDAO;
        this.userRoleDAO = userRoleDAO;
        this.userDAO = userDAO;
        this.roleAccessDAO = roleAccessDAO;
        this.moduleDAO = moduleDAO;
        this.functionDAO = functionDAO;
    }

    public RestResponse login(LoginAccountDTO loginAccountDTO) throws Exception {
        List<ConstraintViolation> violations = new LoginValidator().askForReturn(loginAccountDTO);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }

        RestResponse restResponse = new RestResponse();
        Date now = new Date();
        User user = loginAccountDTO.getUser();
        List<RoleAssignment> roleAssignmentList = roleAssignmentDAO.findByUserIdAndStatus(user.getId(), AppConst.ACCOUNT_STATUS_ACTIVE);
        roleAssignmentList.removeIf(v -> v.getEffectiveDate().after(now) || v.getExpirationDate().before(now));
        List<UserRole> roles = new ArrayList<>();

        for (RoleAssignment roleAssignment : roleAssignmentList) {
            UserRole userRole = userRoleDAO.findById(roleAssignment.getRoleId());
            roles.add(userRole);
        }
        if (roles.isEmpty()) {
            restResponse.setStatus(AppConst.RESPONSE_VALIDATION_FAILED);
            restResponse.setResult(violations);
            violations.add(new ConstraintViolation("password", null,
                    "No Active role", null, String.class));
            throw new ValidationException(violations);
        } else {
            restResponse.setStatus(AppConst.RESPONSE_OK);
            restResponse.setResult(roles);
        }

        return restResponse;
    }

    @RequiresValidation
    public RestResponse selectRole(SelectRoleDTO selectRoleDTO , HttpServletRequest request) throws Exception {
        RestResponse restResponse = new RestResponse();
        Date now = new Date();

        UserRole userRole = userRoleDAO.findByCode(selectRoleDTO.getRoleCode());
        List<RoleAccess> roleAccessList = roleAccessDAO.findByRoleId(userRole.getId());
        for (RoleAccess roleAccess : roleAccessList) {
             roleAccess.setModule(moduleDAO.findById(roleAccess.getModuleId()).getCode());
             roleAccess.setFunction(functionDAO.findById(roleAccess.getFunctionId()).getCode());
        }

        User user = userDAO.findById(selectRoleDTO.getUserId());
        List<RoleAssignment> roleAssignmentList = roleAssignmentDAO.findByUserIdAndStatus(user.getId(), AppConst.ACCOUNT_STATUS_ACTIVE);
        roleAssignmentList.removeIf(v -> v.getEffectiveDate().after(now) || v.getExpirationDate().before(now));

        AppContext appContext = new AppContext();
        appContext.setUsername(user.getUsername());
        appContext.setDisplayName(user.getDisplayName());
        appContext.setDomain(user.getDomain());
        appContext.setLoggedIn(true);
        appContext.setUserId(user.getId().intValue());
        appContext.setCurrentRole(userRole);
        appContext.setAccesses(roleAccessList);
        appContext.setRoleAssignments(roleAssignmentList);
        appContext.setAppContext(request);

        restResponse.setResult(appContext);
        return restResponse;
    }

    @EventListener
    @Transactional
    public void onUserLoginEvent(LoginEvent event) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Date now = new Date();
        User user = event.getUser();
        user.setLastLoginAttemptDt(now);

        if (AppConst.ACCOUNT_STATUS_ACTIVE.equals(event.getLoginAction())) {
            event.setLoginAction(AppConst.YES);
            user.setIsFirstLogin(AppConst.NO);
            user.setLastLoginSuccessDt(now);
            user.setLoginFailedTimes(0);
        } else {
            event.setLoginAction(AppConst.NO);
            user.setLoginFailedTimes(user.getLoginFailedTimes() + 1);
        }

        String times = CommonService.getSetting("admin.login.failedTimes", "10");
        Integer t = StringUtils.parseIfIsInteger(times);
        if (t == null || t <= 0) {
            t = 10;
        }
        if (user.getLoginFailedTimes().equals(t)) {
            user.setStatus(AppConst.ACCOUNT_STATUS_LOCKED);
        }

        LoginTransaction loginTransaction = new LoginTransaction();
        loginTransaction.setUserId(user.getId());
        loginTransaction.setUsername(user.getUsername());
        loginTransaction.setLoginStatus(event.getLoginAction());
        loginTransaction.setStatus(user.getStatus());
        loginTransaction.setIp(SessionManager.getIpAddress(((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest()));

        AppContext appContext = new AppContext();
        appContext.setUsername(user.getUsername());
        appContext.setDomain(user.getDomain());
        BeanUtils.getBean(LoginTransactionDAO.class).save(loginTransaction, appContext);
        user.save(appContext);
        log.info("onUserLoginEvent: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }


    private String generateToken(User user) throws Exception {
        LoginToken loginToken = new LoginToken();
        loginToken.setId(user.getId());
        loginToken.setName(user.getUsername());
        loginToken.setDate(new Date());
        return new ObjectMapper().writeValueAsString(loginToken);
    }
}
