package org.avalon.rainbow.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.admin.entity.Setting;
import org.avalon.rainbow.admin.repository.impl.SettingDAO;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@DependsOn({"beanUtils", "settingDAO"})
public class SettingCache implements ICache<String, Setting> {

    private static final Cache<String, Setting> cache = CacheBuilder.newBuilder().build();

    private SettingCache() {
        init();
    }

    @Override
    public void init() {
        cache.invalidateAll();
        SettingDAO settingDAO = BeanUtils.getBean(SettingDAO.class);
        List<Setting> settings = settingDAO.findAll();
        for (Setting setting : settings) {
            cache.put(setting.getSettingKey(), setting);
        }
    }

    @Override
    public Setting get(String key) {
        return BeanUtils.deepClone(cache.getIfPresent(key));
    }

    @Override
    public void update(String key, Setting value) {
        if (StringUtils.isNotBlank(key) && value != null) {
            cache.put(key, value);
        }
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }
}
