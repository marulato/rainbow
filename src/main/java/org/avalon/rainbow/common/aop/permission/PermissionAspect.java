package org.avalon.rainbow.common.aop.permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.avalon.rainbow.common.base.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class PermissionAspect {

    private static final Logger log = LoggerFactory.getLogger(PermissionAspect.class);
    @Around("@annotation(org.avalon.rainbow.common.aop.permission.RequiresRoles)")
    public Object checkRequiresRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean hasPermission = true;
        Class<?> targetType = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        List<String> roleIds = Arrays.asList(requiresRoles.value());
        Logical logical = requiresRoles.logical();
        AppContext context = AppContext.getFromWebThread();
        return joinPoint.proceed();
    }

    @Around("@annotation(org.avalon.rainbow.common.aop.permission.RequiresLogin)")
    public Object checkRequiresLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> targetType = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        AppContext context = AppContext.getFromWebThread();
        return joinPoint.proceed();
    }

}
