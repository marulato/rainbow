package org.avalon.rainbow.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.admin.repository.impl.MessageDAO;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn({"beanUtils", "messageDAO"})
public class MessageCache implements ICache<String , Message> {

    private static final Cache<String, Message> cache = CacheBuilder.newBuilder().build();

    private MessageCache() {
        init();
    }

    @Override
    public void init() {
        cache.invalidateAll();
        MessageDAO dao = BeanUtils.getBean(MessageDAO.class);
        List<Message> messageList = dao.findAll();
        for (Message message : messageList) {
            cache.put(message.getMsgKey(), message);
        }
    }

    @Override
    public Message get(String key) {
        return BeanUtils.deepClone(cache.getIfPresent(key));
    }

    @Override
    public void update(String key, Message value) {
        if (StringUtils.isNotBlank(key) && value != null) {
            cache.put(key, value);
        }
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }
}
