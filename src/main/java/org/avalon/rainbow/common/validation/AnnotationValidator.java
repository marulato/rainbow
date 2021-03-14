package org.avalon.rainbow.common.validation;

import org.avalon.rainbow.admin.service.CommonService;
import org.avalon.rainbow.common.utils.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AnnotationValidator {

    public static List<ConstraintViolation> validate(Object obj) throws Exception {
        return validate(obj, null);
    }

    public static List<ConstraintViolation> validate(Object obj, String profile) throws Exception {
        List<ConstraintViolation> violations = new ArrayList<>();
        if (obj == null) {
            throw new NullPointerException("Validator class can not be null");
        }
        Class<?> objClass = obj.getClass();
        Field[] allFields = objClass.getDeclaredFields();
        for (Field field : allFields) {
            field.setAccessible(true);
            ConstraintField constraintField = new ConstraintField(field, profile, obj);
            checkAnnotations(constraintField, violations);
        }
        return violations;
    }

    private static boolean isProfileMatch(String profile, String[] annoProfile) {

        if (StringUtils.isEmpty(profile) && (annoProfile == null || annoProfile.length == 0)) {
            return true;
        }
        if (StringUtils.isEmpty(profile) && annoProfile != null && annoProfile.length > 0) {
            return true;
        }
        if (StringUtils.isNotEmpty(profile) && (annoProfile == null || annoProfile.length == 0)) {
            return false;
        }
        if (StringUtils.isNotEmpty(profile) && annoProfile != null && annoProfile.length > 0) {
            List<String> list = Arrays.asList(annoProfile);
            return list.contains(profile);
        }
        return false;
    }

    private static void checkAnnotations(final ConstraintField constraintField,
                                         final List<ConstraintViolation> violations) throws Exception {
        checkAnnotation_AssertNull(constraintField, violations);
        if (violations.isEmpty()) {
            checkAnnotation_NotNull(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_NotEmpty(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_NotBlank(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_MatchesPattern(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_MasterCode(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_MemberOf(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_NotMemberOf(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_Length(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_Email(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_Date(constraintField, violations);
        }
        if (violations.isEmpty()) {
            checkAnnotation_ValidateWithMethod(constraintField, violations);
        }

    }


    private static void checkAnnotation_ValidateWithMethod(ConstraintField constraintField,
                                                           final List<ConstraintViolation> violations) throws Exception {
        Field field = constraintField.getField();
        if (constraintField.isAnnotationPresent(ValidateWithMethod.Group.class)) {
            ValidateWithMethod.Group list = field.getAnnotation(ValidateWithMethod.Group.class);
            ValidateWithMethod[] vars = list.value();
            for (ValidateWithMethod var : vars) {
                validateWithMethod(field, var, constraintField.getInputProfile(), constraintField.getInstanceBelongsTo(), violations);
            }
        } else if (constraintField.isAnnotationPresent(ValidateWithMethod.class)) {
            ValidateWithMethod validateWithMethod = field.getAnnotation(ValidateWithMethod.class);
            validateWithMethod(constraintField.getField(), validateWithMethod, constraintField.getInputProfile(),
                    constraintField.getInstanceBelongsTo(), violations);
        }
    }

    private static void validateWithMethod(Field field, ValidateWithMethod validateWithMethod,
                                           String profile, final Object instance, final List<ConstraintViolation> violations) throws Exception {
        if (isProfileMatch(profile, validateWithMethod.profile())) {
            Object value = Reflections.getValue(field, instance);
            String methodName = validateWithMethod.methodName();
            String[] parameters = validateWithMethod.parameters();
            Class<?> methodInType = validateWithMethod.type();
            Object methodInstance = instance;
            List<Object> argsList = new ArrayList<>();
            Class<?>[] argTypesArray = new Class[parameters.length + 1];
            Arrays.fill(argTypesArray, String.class);
            argTypesArray[0] = field.getType();
            if (methodInType != void.class) {
                methodInstance = methodInType.getConstructor().newInstance();
            }
            Method method = methodInstance.getClass().getDeclaredMethod(methodName, argTypesArray);
            if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) {
                method.setAccessible(true);
                argsList.add(value);
                argsList.addAll(Arrays.asList(parameters));
                boolean result = (boolean) method.invoke(methodInstance, argsList.toArray());
                if (!result) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(validateWithMethod.errorCode(), validateWithMethod.message()),
                            validateWithMethod.profile(), ValidateWithMethod.class));
                }
            } else {
                throw new NoSuchMethodException("Could not find a method that meets the requirements described by '@ValidateWithMethod'");
            }
        }
    }

    private static void checkAnnotation_AssertNull(final ConstraintField constraintField,
                                                   final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(AssertNull.class)) {
            Field field = constraintField.getField();
            AssertNull assertNull = field.getAnnotation(AssertNull.class);
            if (isProfileMatch(constraintField.getInputProfile(), assertNull.profile())) {
                if (Reflections.getValue(field, constraintField.getInstanceBelongsTo()) != null) {
                    violations.add(new ConstraintViolation(field, null,
                            getMessage(assertNull.errorCode(), assertNull.message()), assertNull.profile(), NotNull.class));
                }
            }
        }
    }

    private static void checkAnnotation_NotNull(final ConstraintField constraintField,
                                                final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(NotNull.class)) {
            Field field = constraintField.getField();
            NotNull notNull = field.getAnnotation(NotNull.class);
            if (isProfileMatch(constraintField.getInputProfile(), notNull.profile())) {
                if (Reflections.getValue(field, constraintField.getInstanceBelongsTo()) == null) {
                    violations.add(new ConstraintViolation(field, null,
                            getMessage(notNull.errorCode(), notNull.message()), notNull.profile(), NotNull.class));
                }
            }
        }
    }

    private static void checkAnnotation_NotEmpty(final ConstraintField constraintField,
                                                 final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(NotEmpty.class)) {
            Field field = constraintField.getField();
            NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
            if (isProfileMatch(constraintField.getInputProfile(), notEmpty.profile())) {
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (value == null || StringUtils.isEmpty(String.valueOf(value))) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(notEmpty.errorCode(), notEmpty.message()), notEmpty.profile(), NotNull.class));
                }
            }
        }
    }

    private static void checkAnnotation_NotBlank(final ConstraintField constraintField,
                                                 final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(NotBlank.class)) {
            Field field = constraintField.getField();
            NotBlank notBlank = field.getAnnotation(NotBlank.class);
            if (isProfileMatch(constraintField.getInputProfile(), notBlank.profile())) {
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (value == null || StringUtils.isBlank(String.valueOf(value))) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(notBlank.errorCode(), notBlank.message()), notBlank.profile(), NotBlank.class));
                }
            }
        }
    }

    private static void checkAnnotation_MemberOf(final ConstraintField constraintField,
                                                 final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(MemberOf.class)) {
            Field field = constraintField.getField();
            MemberOf memberOf = field.getAnnotation(MemberOf.class);
            if (isProfileMatch(constraintField.getInputProfile(), memberOf.profile())) {
                List<String> members = List.of(memberOf.value());
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (!(value instanceof String) || !members.contains(String.valueOf(value))) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(memberOf.errorCode(), memberOf.message()), memberOf.profile(), MemberOf.class));
                }
            }
        }
    }

    private static void checkAnnotation_NotMemberOf(final ConstraintField constraintField,
                                                    final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(NotMemberOf.class)) {
            Field field = constraintField.getField();
            NotMemberOf notMemberOf = field.getAnnotation(NotMemberOf.class);
            if (isProfileMatch(constraintField.getInputProfile(), notMemberOf.profile())) {
                List<String> members = List.of(notMemberOf.value());
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (!(value instanceof String) || members.contains(String.valueOf(value))) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(notMemberOf.errorCode(), notMemberOf.message()), notMemberOf.profile(), NotMemberOf.class));
                }
            }
        }
    }

    private static void checkAnnotation_MatchesPattern(final ConstraintField constraintField,
                                                       final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(MatchesPattern.class)) {
            Field field = constraintField.getField();
            MatchesPattern matchesPattern = field.getAnnotation(MatchesPattern.class);
            if (isProfileMatch(constraintField.getInputProfile(), matchesPattern.profile())) {
                Pattern pattern = Pattern.compile(matchesPattern.pattern());
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (!(value instanceof String) || !pattern.matcher(String.valueOf(value)).matches()) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(matchesPattern.errorCode(), matchesPattern.message()),
                            matchesPattern.profile(), MatchesPattern.class));
                }
            }
        }
    }

    private static void checkAnnotation_Length(final ConstraintField constraintField,
                                               final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(Length.class)) {
            Field field = constraintField.getField();
            Length length = field.getAnnotation(Length.class);
            if (isProfileMatch(constraintField.getInputProfile(), length.profile())) {
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (value == null && length.min() == 0) {
                    return;
                }
                if (!(value instanceof String) || ((String) value).length() < length.min()
                        || ((String) value).length() > length.max()) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(length.errorCode(), length.message()), length.profile(), Length.class));
                }
            }
        }
    }

    private static void checkAnnotation_Email(final ConstraintField constraintField,
                                              final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(Email.class)) {
            Field field = constraintField.getField();
            Email email = field.getAnnotation(Email.class);
            if (isProfileMatch(constraintField.getInputProfile(), email.profile())) {
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (!(value instanceof String) || !ValidationUtils.isValidEmail((String) value)) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(email.errorCode(), email.message()), email.profile(), Email.class));
                }
            }
        }
    }

    private static void checkAnnotation_Date(final ConstraintField constraintField,
                                             final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(Date.class)) {
            Field field = constraintField.getField();
            Date date = field.getAnnotation(Date.class);
            if (isProfileMatch(constraintField.getInputProfile(), date.profile())) {
                Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
                if (!(value instanceof String) || DateUtils.parseDatetime((String) value, date.pattern()) == null) {
                    violations.add(new ConstraintViolation(field, value,
                            getMessage(date.errorCode(), date.message()), date.profile(), Date.class));
                }
            }
        }
    }

    private static void checkAnnotation_MasterCode(final ConstraintField constraintField,
                                                   final List<ConstraintViolation> violations) throws Exception {
        if (constraintField.isAnnotationPresent(MasterCode.class)) {
            Field field = constraintField.getField();
            MasterCode masterCode = field.getAnnotation(MasterCode.class);
            String type = masterCode.type();
            CommonService commonService = BeanUtils.getBean(CommonService.class);
            List<org.avalon.rainbow.admin.entity.MasterCode> masterCodeList = commonService.getMasterCodeByType(type);
            Object value = Reflections.getValue(field, constraintField.getInstanceBelongsTo());
            for (org.avalon.rainbow.admin.entity.MasterCode code : masterCodeList) {
                if (code.getCode().equals(value)) {
                    return;
                }
            }
            violations.add(new ConstraintViolation(field, value,
                    getMessage(masterCode.errorCode(), masterCode.message()), masterCode.profile(), MasterCode.class));
        }
    }

    private static String getMessage(String errorCode, String message) {
        if ("err.default".equals(errorCode)) {
            return StringUtils.isEmpty(message) ? "Invalid Value" : message;
        }
        String[] errorKeys = errorCode.split(",");
        for (int i = 0; i < errorKeys.length; i++) {
            errorKeys[i] = errorKeys[i].trim();
        }
        String[] replacements = ArrayUtils.extract(errorKeys, 1, errorKeys.length - 1);
        String errorMsg = CommonService.getMessage(errorKeys[0], replacements);
        return StringUtils.isEmpty(errorMsg) ? "Invalid Value" : errorMsg;
    }

}
