package org.avalon.rainbow.admin.repository.impl;

import org.apache.commons.collections4.IterableUtils;
import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.admin.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDAO {

    private final MessageRepository messageRepository;

    public MessageDAO(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAll() {
        return IterableUtils.toList(messageRepository.findAll());
    }
}
