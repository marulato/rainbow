package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

}
