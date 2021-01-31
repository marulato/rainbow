package org.avalon.rainbow.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.admin.service.MessageService;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.SpringUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import java.util.List;

public class MessageCache implements ICache<String , Message> {

    private static final Cache<String, Message> cache = CacheBuilder.newBuilder().build();

    public static MessageCache getInstance() {
        if (cache.size() == 0) {
            return new MessageCache(true);
        } else {
            return new MessageCache(false);
        }
    }

    private MessageCache(boolean init) {
        if (init) {
            init();
        }
    }

    @Override
    public void init() {
        cache.invalidateAll();
        MessageService service = SpringUtils.getBean(MessageService.class);
        List<Message> messageList = service.selectAll();
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
