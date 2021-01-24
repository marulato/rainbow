package org.avalon.rainbow.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import java.io.Serializable;


public class BeanUtils {

    public static<T extends Serializable> T deepClone(T entity) {
        if (entity != null) {
            return SerializationUtils.clone(entity);
        }
        return null;
    }

    public static <T> T deepClone(T entity, Class<T> type) {
        if (entity != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(entity), type);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
        return null;
    }

}
