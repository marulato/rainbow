package org.avalon.rainbow.common.validation;

import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator {

    private final List<ConstraintViolation> violations = new ArrayList<>();

    protected abstract void verify(Object object);

    public void validate(Object object) {
        verify(object);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    public void addViolation(String name, String message) {
        if (StringUtils.isNotBlank(name)) {
            ConstraintViolation violation = new ConstraintViolation();
            violation.setValidatedFieldName(name);
            violation.setMessage(message);
            violations.add(violation);
        }
    }
}
