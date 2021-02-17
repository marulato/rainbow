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

    public void save(T po) {
        if (po != null) {
            if (po.getId() != null && po.getId() > 0) {
                po.updateAuditValues(AppContext.getFromWebThread());
            } else {
                po.createAuditValues(AppContext.getFromWebThread());
            }
            crudRepository.save(po);
        }
    }

    public void saveAll(List<T> list) {
        if (list != null) {
            for (T t : list) {
                if (t.getId() != null && t.getId() > 0) {
                    t.updateAuditValues(AppContext.getFromWebThread());
                } else {
                    t.createAuditValues(AppContext.getFromWebThread());
                }
            }
            crudRepository.saveAll(list);
        }
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
