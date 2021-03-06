package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.EmailTransaction;
import org.avalon.rainbow.admin.repository.EmailRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmailTransactionDAO extends CrudDAO<EmailTransaction, Long, EmailRepository> {

    @Autowired
    protected EmailTransactionDAO(EmailRepository crudRepository) {
        super(crudRepository);
    }


}
