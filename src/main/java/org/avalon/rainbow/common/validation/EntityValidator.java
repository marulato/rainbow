package org.avalon.rainbow.common.validation;

import java.lang.reflect.Method;

public class EntityValidator {

    public static void validate(Object object) throws Exception {
        if (object == null) {
            throw new NullPointerException("Validator class can not be null");
        }
        Class<?> type = object.getClass();
        if (type.isAnnotationPresent(Validate.class)) {
            Class<?> validatorClass = type.getAnnotation(Validate.class).value();
            if (validatorClass == NoValidator.class)
                return;

            Object validatorEntity = validatorClass.getConstructor().newInstance();
            Class<?> abstractValidator = validatorClass.getSuperclass();
            if (abstractValidator != AbstractValidator.class) {
                throw new IllegalArgumentException("Validator class NOT Supported");
            }
            Method method = abstractValidator.getMethod("askForException", Object.class);
            method.invoke(validatorEntity, object);
        }
    }
}
