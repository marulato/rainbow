package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.EmailTransaction;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<EmailTransaction, Long> {
}
