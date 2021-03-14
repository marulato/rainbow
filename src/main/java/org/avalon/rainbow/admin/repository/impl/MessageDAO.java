package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.admin.repository.MessageRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MessageDAO extends CrudDAO<Message, Long, MessageRepository> {

    @Autowired
    protected MessageDAO(MessageRepository crudRepository) {
        super(crudRepository);
    }

    public Message findByKey(String key) {
        return crudRepository.findMessageByMsgKey(key);
    }
}
