package org.avalon.rainbow.common.jpa;

import org.apache.commons.collections4.IterableUtils;
import org.avalon.rainbow.common.base.AppContext;
import org.avalon.rainbow.common.base.BasePO;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public abstract class CrudDAO <T extends BasePO, ID, E extends CrudRepository<T, ID>> {

    private final E crudRepository;

    protected CrudDAO(E crudRepository) {
        this.crudRepository = crudRepository;
    }

    public void save(T po, AppContext appContext) {
        if (po != null) {
            if (po.getId() != null && po.getId() > 0) {
                po.updateAuditValues(appContext);
            } else {
                po.createAuditValues(appContext);
            }
            crudRepository.save(po);
        }
    }

    public void save(T po) {
        save(po, AppContext.getFromWebThread());
    }

    public void saveAll(List<T> list, AppContext appContext) {
        if (list != null) {
            for (T t : list) {
                if (t.getId() != null && t.getId() > 0) {
                    t.updateAuditValues(appContext);
                } else {
                    t.createAuditValues(appContext);
                }
            }
            crudRepository.saveAll(list);
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
}
