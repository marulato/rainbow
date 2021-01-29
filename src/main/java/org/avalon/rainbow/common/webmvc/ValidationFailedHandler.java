package org.avalon.rainbow.common.webmvc;

import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.validation.ConstraintViolation;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@ControllerAdvice
public class ValidationFailedHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public List<ConstraintViolation> returnValidation(ValidationException e) {
        return e.getViolations();
    }

}
