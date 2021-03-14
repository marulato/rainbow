package org.avalon.rainbow.common.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.avalon.rainbow.common.base.RestResponse;
import org.avalon.rainbow.common.base.ValidationResponse;
import org.avalon.rainbow.common.constant.AppConst;
import org.avalon.rainbow.common.ex.AccessDeniedException;
import org.avalon.rainbow.common.ex.ValidationException;
import org.avalon.rainbow.common.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<ValidationResponse> returnValidation(ValidationException e) throws Exception {
        ValidationResponse validationResponse = new ValidationResponse();
        List<ConstraintViolation> violations = e.getViolations();
        if (violations != null) {
            for (ConstraintViolation violation : violations) {
                validationResponse.addFieldMsg(violation);
                validationResponse.setException(e.getMessage());
            }
        }
        log.info(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(violations));
        return ResponseEntity.badRequest().body(validationResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<?> returnValidation(AccessDeniedException e) throws Exception {
        return new ResponseEntity<> (HttpStatus.UNAUTHORIZED);
    }

}
