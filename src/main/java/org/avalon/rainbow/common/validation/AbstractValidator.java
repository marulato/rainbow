package org.avalon.rainbow.common.validation;

import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValidator {

    private final List<ConstraintViolation> violations = new ArrayList<>();

    protected abstract void validate(Object object);

    public List<ConstraintViolation> askForReturn(Object object) {
        validate(object);
        return violations;
    }

    public void askForException(Object object) {
        validate(object);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    protected void addViolation(String name, String message) {
        if (StringUtils.isNotBlank(name)) {
            ConstraintViolation violation = new ConstraintViolation();
            violation.setValidatedFieldName(name);
            violation.setMessage(message);
            violations.add(violation);
        }
    }

}
