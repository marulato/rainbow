package org.avalon.rainbow.common.jpa.exec;

import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.utils.Reflections;
import org.avalon.rainbow.common.utils.SpringUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import java.lang.reflect.Field;
import java.util.Date;

public class BaseDAO {

    private static final IExecutor executor = SpringUtils.getBean(IExecutor.class);

    public static void save(BasePO entity) {
        if (entity != null) {
            Date now = new Date();
            AppContext appContext = AppContext.getFromWebThread();
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            entity.setCreatedBy(appContext.getUserId());
            entity.setCreatedDm(appContext.getDomain());
            entity.setUpdatedBy(appContext.getUserId());
            entity.setUpdatedDm(appContext.getDomain());
            formatEmptyString(entity);
            executor.insert(entity);
        }
    }

    public static void update(BasePO entity) {
        if (entity != null) {
            Date now = new Date();
            AppContext appContext = AppContext.getFromWebThread();
            entity.setUpdatedAt(now);
            entity.setUpdatedBy(appContext.getUserId());
            entity.setUpdatedDm(appContext.getDomain());
            formatEmptyString(entity);
            executor.update(entity);
        }
    }

    public static void delete(BasePO entity) {
        if (entity != null) {
            executor.delete(entity);
        }
    }

    private static void formatEmptyString(BasePO po) {
        Class<?> type = po.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                try {
                    String value = (String) Reflections.getValue(field, po);
                    if (StringUtils.isEmpty(value)) {
                        field.set(po, null);
                    }
                } catch (Exception ignored) {

                }
            }
        }
    }
}
