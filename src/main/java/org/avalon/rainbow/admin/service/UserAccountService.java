package org.avalon.rainbow.admin.service;

import com.google.common.base.Stopwatch;
import org.avalon.rainbow.admin.dto.LoginAccountDTO;
import org.avalon.rainbow.admin.dto.LoginResult;
import org.avalon.rainbow.admin.dto.SelectRoleDTO;
import org.avalon.rainbow.admin.entity.*;
import org.avalon.rainbow.admin.repository.impl.*;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.base.RestResponse;
import org.avalon.rainbow.common.base.SessionManager;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.EncryptionUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Transactional
    public LoginResult login(LoginAccountDTO loginAccountDTO) {
        LoginResult result = validateLogin(loginAccountDTO);
        Date now = new Date();
        User user = loginAccountDTO.getUser();
        List<RoleAssignment> roleAssignmentList = roleAssignmentDAO.findByUserIdAndStatus(user.getId(), AppConst.ACCOUNT_STATUS_ACTIVE);
        roleAssignmentList.removeIf(v -> v.getEffectiveDate().after(now) || v.getExpirationDate().before(now));
        List<UserRole> roles = new ArrayList<>();

        for (RoleAssignment roleAssignment : roleAssignmentList) {
            UserRole userRole = userRoleDAO.findById(roleAssignment.getRoleId());
            roles.add(userRole);
        }
        result.setRoles(roles);
        trackLoginHistory(result);

        return result;
    }

    public RestResponse selectRole(SelectRoleDTO selectRoleDTO , HttpServletRequest request){
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

    public void trackLoginHistory(LoginResult result) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Date now = new Date();
        User user = result.getUser();
        user.setLastLoginAttemptDt(now);

        if (result.isSuccess()) {
            user.setIsFirstLogin(AppConst.NO);
            user.setLastLoginSuccessDt(now);
            user.setLoginFailedTimes(0);
        } else if (!result.isPwdCorrect()){
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
        loginTransaction.setLoginStatus(result.isSuccess() ? AppConst.YES : AppConst.NO);
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

    public LoginResult validateLogin(LoginAccountDTO dto) {
        User user = dto.getUser();
        if (user == null) {
            return LoginResult.incorrectPassword(null);
        } else {
            String status = user.updateStatus();
            boolean isPwdMatch = EncryptionUtils.matchPassword(dto.getPassword(), user.getPassword());
            if (!isPwdMatch) {
                return LoginResult.incorrectPassword(user);
            } else {
                return LoginResult.status(status, user);
            }
        }
    }
}
