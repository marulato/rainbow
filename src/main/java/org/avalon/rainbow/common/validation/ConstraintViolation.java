package org.avalon.rainbow.common.validation;

import java.lang.reflect.Field;

public class ConstraintViolation {

    private String validatedFieldName;
    private Object validatedFieldValue;
    private String message;
    private String[] profile;
    private Class<?> validationType;
    private Class<?> fieldType;

    public ConstraintViolation(Field field, Object value, String message, String[] profile, Class<?> validationType) {
        this.validatedFieldName = field.getName();
        this.validatedFieldValue = value;
        this.message = message;
        this.profile = profile;
        this.validationType = validationType;
        this.fieldType = field.getType();
    }

    public ConstraintViolation(String fieldName, Object value, String message, String[] profile, Class<?> validationType) {
        this.validatedFieldName = fieldName;
        this.validatedFieldValue = value;
        this.message = message;
        this.profile = profile;
        this.validationType = validationType;
        if (value != null) {
            this.fieldType = value.getClass();
        }
    }

    public ConstraintViolation() {}

    public String getValidatedFieldName() {
        return validatedFieldName;
    }

    public void setValidatedFieldName(String validatedFieldName) {
        this.validatedFieldName = validatedFieldName;
    }

    public Object getValidatedFieldValue() {
        return validatedFieldValue;
    }

    public void setValidatedFieldValue(Object validatedFieldValue) {
        this.validatedFieldValue = validatedFieldValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getProfile() {
        return profile;
    }

    public void setProfile(String[] profile) {
        this.profile = profile;
    }

    public Class<?> getValidationType() {
        return validationType;
    }

    public void setValidationType(Class<?> validationType) {
        this.validationType = validationType;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }
}
