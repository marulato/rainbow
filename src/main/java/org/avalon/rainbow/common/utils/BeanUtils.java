package org.avalon.rainbow.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Serializable;

@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        return appContext.getBean(clazz);
    }

    public static DataSource getDataSource() {
        return getBean(DataSource.class);
    }

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

    public static ApplicationContext getApplicationContext() {
        return appContext;
    }
}
