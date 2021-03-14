package org.avalon.rainbow.common.jpa;

import org.apache.commons.collections4.IterableUtils;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.base.BasePO;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.Reflections;
import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudDAO <T extends BasePO, ID, E extends CrudRepository<T, ID>> {

    protected final E crudRepository;

    protected CrudDAO(E crudRepository) {
        this.crudRepository = crudRepository;
    }

    public void save(T po, AppContext appContext) {
        if (po != null) {
            T obj = clearEmpty(po);
            if (obj.getId() != null && obj.getId() > 0) {
                obj.updateAuditValues(appContext);
            } else {
                obj.createAuditValues(appContext);
            }
            crudRepository.save(obj);
        }
    }

    public void save(T po) {
        save(po, AppContext.getFromWebThread());
    }

    public void saveAll(List<T> list, AppContext appContext) {
        if (list != null) {
            List<T> saveList = new ArrayList<>(list.size());
            for (T t : list) {
                T obj = clearEmpty(t);
                if (obj.getId() != null && obj.getId() > 0) {
                    obj.updateAuditValues(appContext);
                } else {
                    obj.createAuditValues(appContext);
                }
                saveList.add(obj);
            }
            crudRepository.saveAll(saveList);
        }
    }

    public void saveAll(List<T> list) {
        saveAll(list, AppContext.getFromWebThread());
    }

    public List<T> findAll() {
        return IterableUtils.toList(crudRepository.findAll());
    }

    public T findById(ID id) {
        return crudRepository.findById(id).orElse(null);
    }

    public void deleteById(ID id) {
        crudRepository.deleteById(id);
    }

    public void delete(T t) {
        crudRepository.delete(t);
    }

    private T clearEmpty(T t) {
        T t2 = BeanUtils.deepClone(t);
        Field[] fields = t2.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getType() == String.class && StringUtils.isBlank((String) field.get(t2))) {
                    field.set(t2, null);
                }
            } catch (Exception ignored) {

            }
        }
        return t2;
    }
}
