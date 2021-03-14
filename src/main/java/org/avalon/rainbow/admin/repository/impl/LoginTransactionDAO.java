package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.LoginTransaction;
import org.avalon.rainbow.admin.repository.LoginTransactionRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class LoginTransactionDAO extends CrudDAO<LoginTransaction, Long, LoginTransactionRepository> {

    protected LoginTransactionDAO(LoginTransactionRepository crudRepository) {
        super(crudRepository);
    }
}
