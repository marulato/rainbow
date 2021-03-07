package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Functionality;
import org.avalon.rainbow.admin.repository.FunctionRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FunctionDAO extends CrudDAO<Functionality, Long, FunctionRepository> {
    @Autowired
    protected FunctionDAO(FunctionRepository crudRepository) {
        super(crudRepository);
    }
}
