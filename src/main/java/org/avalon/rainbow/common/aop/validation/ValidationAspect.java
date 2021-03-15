package org.avalon.rainbow.common.aop.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.utils.ArrayUtils;
import org.avalon.rainbow.common.validation.AnnotationValidator;
import org.avalon.rainbow.common.validation.BindingResult;
import org.avalon.rainbow.common.validation.ConstraintViolation;
import org.avalon.rainbow.common.validation.EntityValidator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@Aspect
@Component
public class ValidationAspect {

    @Around("@annotation(org.avalon.rainbow.common.aop.validation.Validated)")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> targetType = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs();
        int bindingResultIndex = -1;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        Parameter[] parameters = method.getParameters();
        String[] paramNames = methodSignature.getParameterNames();
        Class<?>[] paramTypes =  methodSignature.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (paramTypes[i] == BindingResult.class) {
                bindingResultIndex = i;
                break;
            }
        }
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(Validate.class)) {
                Validate reqVal = parameter.getAnnotation(Validate.class);
                if (ValidationType.ANNOTATION.equals(reqVal.value())) {
                    List<ConstraintViolation> violations = AnnotationValidator
                            .validate(args[ArrayUtils.indexOf(paramNames, parameter.getName())], reqVal.profile());
                    if (!violations.isEmpty()) {
                        if (bindingResultIndex < 0) {
                            throw new ValidationException(violations);
                        }
                        args[bindingResultIndex] = new BindingResult(violations);
                    }
                } else if (ValidationType.VALIDATOR.equals(reqVal.value())) {
                    List<ConstraintViolation> violations  =
                            EntityValidator.validate(args[ArrayUtils.indexOf(paramNames, parameter.getName())]);
                    if (!violations.isEmpty()) {
                        if (bindingResultIndex < 0) {
                            throw new ValidationException(violations);
                        }
                        args[bindingResultIndex] = new BindingResult(violations);
                    }
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
