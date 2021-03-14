package org.avalon.rainbow.common.aop.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.validation.AnnotationValidator;
import org.avalon.rainbow.common.validation.ConstraintViolation;
import org.avalon.rainbow.common.validation.EntityValidator;
import org.avalon.rainbow.common.validation.Validate;
import org.springframework.stereotype.Component;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class ValidationAspect {

    @Before("@annotation(org.avalon.rainbow.common.aop.validation.RequiresValidation)")
    public void validate(JoinPoint joinPoint) throws Throwable {
        Class<?> targetType = joinPoint.getTarget().getClass();
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = targetType.getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        RequiresValidation reqVal = method.getAnnotation(RequiresValidation.class);
        for (Object arg : args) {
            if (arg instanceof ServletRequest || arg instanceof ServletResponse) {
                continue;
            }
            if (ValidationType.ANNOTATION.equals(reqVal.value())) {
                List<ConstraintViolation> violations = AnnotationValidator.validate(arg, reqVal.profile());
                if (!violations.isEmpty()) {
                    throw new ValidationException(violations);
                }
            } else if (ValidationType.VALIDATOR.equals(reqVal.value())) {
                EntityValidator.validate(arg);
            }
        }
    }
}
