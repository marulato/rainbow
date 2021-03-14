package org.avalon.rainbow.admin.controller;

import org.avalon.rainbow.common.docgen.ExcelGenerator;

import java.util.HashMap;
import java.util.Map;

public class TestGenerator extends ExcelGenerator {
    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "yinjing");
        return map;
    }

    @Override
    public String getTemplate() {
        return "test.xlsx";
    }
}
