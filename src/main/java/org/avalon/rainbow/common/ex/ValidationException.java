package org.avalon.rainbow.common.ex;

import org.avalon.rainbow.common.validation.ConstraintViolation;
import java.util.List;

public class ValidationException extends RuntimeException{

    private List<ConstraintViolation> violations;

    public ValidationException(List<ConstraintViolation> violations) {
        super();
        this.violations = violations;
    }

    public List<ConstraintViolation> getViolations() {
        return violations;
    }

    public void setViolations(List<ConstraintViolation> violations) {
        this.violations = violations;
    }
}
