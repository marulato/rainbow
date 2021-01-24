package org.avalon.rainbow.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.common.utils.StringUtils;

public class CacheFactory {

    private static final Cache<String, ICache<?, ?>> cachePool = CacheBuilder.newBuilder().build();

    public static ICache<?, ?> getCache(String name) {
        return cachePool.getIfPresent(name);
    }

    public static void putCache(String name, ICache<?, ?> cache) {
        if (StringUtils.isNotBlank(name) && cache != null) {
            cachePool.put(name, cache);
        }
    }
}
