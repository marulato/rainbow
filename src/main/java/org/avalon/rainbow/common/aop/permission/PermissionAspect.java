package org.avalon.rainbow.common.aop.permission;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.avalon.rainbow.admin.entity.Functionality;
import org.avalon.rainbow.admin.entity.Module;
import org.avalon.rainbow.admin.entity.RoleAccess;
import org.avalon.rainbow.admin.entity.UserRole;
import org.avalon.rainbow.admin.repository.impl.FunctionDAO;
import org.avalon.rainbow.admin.repository.impl.ModuleDAO;
import org.avalon.rainbow.common.base.AccessConst;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.ex.AccessDeniedException;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@DependsOn({"moduleDAO", "functionDAO", "beanUtils"})
public class PermissionAspect {

    private static final Logger log = LoggerFactory.getLogger(PermissionAspect.class);
    private static final Map<String, Module> moduleMap = new HashMap<>(75);
    private static final Map<String, Functionality> functionalityMap = new HashMap<>(750);

    public PermissionAspect() {
        ModuleDAO moduleDAO = BeanUtils.getBean(ModuleDAO.class);
        FunctionDAO functionDAO = BeanUtils.getBean(FunctionDAO.class);

        List<Module> modules = moduleDAO.findAll();
        List<Functionality> functionalities = functionDAO.findAll();

        for (Module module : modules) {
            moduleMap.put(module.getCode(), module);
        }
        for (Functionality functionality : functionalities) {
            functionalityMap.put(functionality.getCode(), functionality);
        }
    }


    @Before("@annotation(org.avalon.rainbow.common.aop.permission.RequiresRoles)")
    public void checkRequiresRoles(JoinPoint joinPoint) throws Throwable {
        boolean hasPermission = true;
        Class<?> targetType = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);

        List<String> roleIds = Arrays.asList(requiresRoles.value());
        AppContext context = AppContext.getFromWebThread();
        UserRole userRole = context.getCurrentRole();
        String[] allowedRoles = requiresRoles.value();
        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(userRole.getCode())) {
                return;
            }
        }
        throw new AccessDeniedException();
    }

    @Before("@annotation(org.avalon.rainbow.common.aop.permission.RequiresLogin)")
    public void checkRequiresLogin(JoinPoint joinPoint) throws Throwable {
        Class<?> targetType = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        AppContext context = AppContext.getFromWebThread();
    }

    @Before("@annotation(org.avalon.rainbow.common.aop.permission.AccessControl)")
    public void checkFunctionAccess(JoinPoint joinPoint) throws Throwable {
        Class<?> targetType = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        AccessControl accessControl = method.getAnnotation(AccessControl.class);
        Module module = moduleMap.get(accessControl.module());
        Functionality function = functionalityMap.get(accessControl.function());
        String operation = accessControl.operation();
        AppContext context = AppContext.getFromWebThread();
        if (context == null) {
            throw new AccessDeniedException();
        }
        boolean hasAccess = false;

        for (RoleAccess access : context.getAccesses()) {
            if (access.getModuleId().equals(module.getId()) && access.getFunctionId().equals(function.getId())) {
                switch (operation) {
                    case AccessConst.REQUEST_METHOD_GET:
                        hasAccess = AppConst.YES.equals(access.getGet());
                        break;
                    case AccessConst.REQUEST_METHOD_POST:
                        hasAccess = AppConst.YES.equals(access.getPost());
                        break;
                    case AccessConst.REQUEST_METHOD_PUT:
                        hasAccess = AppConst.YES.equals(access.getPut());
                        break;
                    case AccessConst.REQUEST_METHOD_PATCH:
                        hasAccess = AppConst.YES.equals(access.getPatch());
                        break;
                    case AccessConst.REQUEST_METHOD_DELETE:
                        hasAccess = AppConst.YES.equals(access.getDelete());
                        break;
                    default:
                        hasAccess = false;
                        break;
                }
            }
        }

        if (!hasAccess) {
            throw new AccessDeniedException();
        }
    }

}
