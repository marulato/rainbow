package org.avalon.rainbow.common.base;

import org.avalon.rainbow.common.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationResponse implements Serializable {

    private int errors;
    private String exception;
    private List<ValidationKV> results;

    public ValidationResponse() {
        this.results = new ArrayList<>();
    }

    public void addFieldMsg(ConstraintViolation violation) {
        if (violation != null) {
            errors ++;
            ValidationKV kv = new ValidationKV();
            kv.setField(violation.getValidatedFieldName());
            kv.setMessage(violation.getMessage());
            String type = violation.getFieldType() != null ? violation.getFieldType().toString() : null;
            if (type != null) {
                kv.setType(type.substring(type.lastIndexOf(".") + 1));
            }
            results.add(kv);
        }
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public List<ValidationKV> getResults() {
        return results;
    }

    public void setResults(List<ValidationKV> results) {
        this.results = results;
    }

    private static class ValidationKV {
        private String field;
        private String message;
        private String type;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


}
