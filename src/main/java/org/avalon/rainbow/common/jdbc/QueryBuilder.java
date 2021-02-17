package org.avalon.rainbow.common.jdbc;

import org.avalon.rainbow.common.utils.Reflections;
import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class QueryBuilder <T> {

    private RowMapper<T> rowMapper;
    private final StringBuilder sqlBuilder;
    private final List<Object> values;

    public QueryBuilder(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
        sqlBuilder = new StringBuilder();
        values = new ArrayList<>();
    }

    public QueryBuilder(Class<T> returnType) {
        sqlBuilder = new StringBuilder();
        values = new ArrayList<>();
        Field[] fields = returnType.getDeclaredFields();
        this.rowMapper = (rs, i) -> {
            try {
                T obj = returnType.getConstructor().newInstance();
                Class<?> superclass = returnType.getSuperclass();
                if (superclass != null && superclass != Object.class) {
                    Field[] superFields = superclass.getDeclaredFields();
                    for (Field field : superFields) {
                        if (!field.isAnnotationPresent(NotColumn.class)) {
                            setResultSetValues(rs, obj, field, superclass);
                        }
                    }
                }
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(NotColumn.class)) {
                        setResultSetValues(rs, obj, field, returnType);
                    }
                }
                return obj;
            } catch (Exception e) {
                throw new IllegalArgumentException("Class: [" + returnType.getName()
                        + "] Can not be instantiated: " + e.getMessage());
            }
        };
    }

    private String getColumnName(String fieldName) {
        String[] words = StringUtils.splitByUpperCase(fieldName);
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word.toUpperCase()).append("_");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private void setResultSetValues(ResultSet rs, Object instance, Field field, Class<?> superClass) throws Exception {
        Object value = null;
        if (field.getType() == String.class) {
            value = rs.getString(getColumnName(field.getName()));
        } else if (field.getType() == Date.class) {
            value = rs.getTimestamp(getColumnName(field.getName()));
        } else if (field.getType() == Long.class) {
            value = rs.getLong(getColumnName(field.getName()));
        } else if (field.getType() == byte[].class) {
            value = rs.getBytes(getColumnName(field.getName()));
        } else if (field.getType() == Integer.class) {
            value = rs.getInt(getColumnName(field.getName()));
        }
        Reflections.setSuperValue(field, instance, value, superClass);
    }

    public Object[] getParameters() {
        return values.toArray(new Object[0]);
    }

    public void addParameter(Object o) {
        values.add(o);
    }

    public void appendSQL(Object sql) {
        sqlBuilder.append(sql);
    }

    public void appendSQL(Object sql, Object... parameter) {
        sqlBuilder.append(sql);
        if (parameter != null) {
            Collections.addAll(values, parameter);
        }
    }

    public String getSql() {
        return sqlBuilder.toString();
    }

    public RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public List<Object> getValues() {
        return values;
    }
}
