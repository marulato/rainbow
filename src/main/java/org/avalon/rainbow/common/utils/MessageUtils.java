package org.avalon.rainbow.common.utils;

import org.avalon.rainbow.admin.entity.Message;
import org.avalon.rainbow.common.cache.MessageCache;
import java.text.MessageFormat;

public class MessageUtils {

    public static String getMessage(String key, Object[] replacements) {
        MessageCache cache = MessageCache.getInstance();
        Message message =  cache.get(key);
        if (message != null) {
            if (replacements == null || replacements.length == 0) {
                return message.getMsgValue();
            } else {
                return MessageFormat.format(message.getMsgValue(), replacements);
            }
        }
        return null;
    }

    public static String getMessage(String key) {
        return getMessage(key, null);
    }

}
