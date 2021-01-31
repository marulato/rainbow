package org.avalon.rainbow.common.webmvc;

import org.avalon.rainbow.common.base.ValidationRV;
import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.validation.ConstraintViolation;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ValidationFailedHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public List<ValidationRV> returnValidation(ValidationException e) {
        List<ConstraintViolation> violations = e.getViolations();
        List<ValidationRV> validationRVList = new ArrayList<>();
        if (violations != null) {
            for (ConstraintViolation violation : violations) {
                ValidationRV rv = new ValidationRV();
                rv.setName(violation.getValidatedFieldName());
                rv.setMessage(violation.getMessage());
                validationRVList.add(rv);
            }
        }
        return validationRVList;
    }

}
