package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.admin.repository.MessageRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDAO extends CrudDAO<Message, Long, MessageRepository> {

    private final MessageRepository repository;

    protected MessageDAO(MessageRepository crudRepository) {
        super(crudRepository);
        this.repository = crudRepository;
    }

    public Message findByKey(String key) {
        return repository.findMessageByMsgKey(key);
    }
}
