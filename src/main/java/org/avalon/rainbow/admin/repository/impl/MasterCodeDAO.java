package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.MasterCode;
import org.avalon.rainbow.admin.repository.MasterCodeRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MasterCodeDAO extends CrudDAO<MasterCode, Long, MasterCodeRepository> {


    @Autowired
    public MasterCodeDAO(MasterCodeRepository crudRepository) {
        super(crudRepository);
    }
}
