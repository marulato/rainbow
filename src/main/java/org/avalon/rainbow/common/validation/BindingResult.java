package org.avalon.rainbow.common.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingResult {

    private List<ConstraintViolation> violations;
    private final Map<String, ConstraintViolation> map;

    public BindingResult(List<ConstraintViolation> violations) {
        this.violations = violations;
        if (violations != null) {
            map = new HashMap<>(violations.size());
            for (ConstraintViolation violation : violations) {
                map.put(violation.getValidatedFieldName(), violation);
            }
        } else {
            this.violations = new ArrayList<>();
            map = new HashMap<>();
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(violations);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public BindingResult() {
        this.violations = new ArrayList<>();
        map = new HashMap<>();
    }

    public ConstraintViolation getViolation(String fieldName) {
        return map.get(fieldName);
    }

    public boolean hasErrors() {
        return !violations.isEmpty();
    }

    public int getErrorCounts() {
        return violations.size();
    }

    public List<ConstraintViolation> getViolations() {
        return violations;
    }

    public void setViolations(List<ConstraintViolation> violations) {
        this.violations = violations;
    }
}
