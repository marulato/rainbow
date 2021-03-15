package org.avalon.rainbow.common.validation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntityValidator {

    @SuppressWarnings("unchecked")
    public static List<ConstraintViolation> validate(Object object) throws Exception {
        if (object == null) {
            throw new NullPointerException("Validator class can not be null");
        }
        Class<?> type = object.getClass();
        if (type.isAnnotationPresent(Validator.class)) {
            Class<?> validatorClass = type.getAnnotation(Validator.class).value();
            if (validatorClass == NoValidator.class)
                return new ArrayList<>();

            Object validatorEntity = validatorClass.getConstructor().newInstance();
            Class<?> abstractValidator = validatorClass.getSuperclass();
            if (abstractValidator != AbstractValidator.class) {
                throw new IllegalArgumentException("Validator class NOT Supported");
            }
            Method method = abstractValidator.getMethod("askForReturn", Object.class);
            return (List<ConstraintViolation>) method.invoke(validatorEntity, object);
        }
        return new ArrayList<>();
    }
}
