package org.avalon.rainbow.admin.service;

import org.avalon.rainbow.admin.dao.MessageDAO;
import org.avalon.rainbow.admin.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    private final MessageDAO messageDAO;

    @Autowired
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> selectAll() {
        return messageDAO.selectAll();
    }
}
