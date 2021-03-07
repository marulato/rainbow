package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Module;
import org.avalon.rainbow.admin.repository.ModuleRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleDAO extends CrudDAO<Module, Long, ModuleRepository> {

    @Autowired
    protected ModuleDAO(ModuleRepository crudRepository) {
        super(crudRepository);
    }
}
