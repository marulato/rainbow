package org.avalon.rainbow.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.admin.entity.MasterCode;
import org.avalon.rainbow.admin.service.MasterCodeService;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.NumberUtils;
import org.avalon.rainbow.common.utils.SpringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MasterCodeCache implements ICache<Integer, MasterCode> {

    private static final Cache<Integer, MasterCode> idCache = CacheBuilder.newBuilder().build();
    private static final Cache<String, List<MasterCode>> typeCache = CacheBuilder.newBuilder().build();

    public MasterCodeCache getInstance() {
        if (idCache.size() == 0 || typeCache.size() == 0) {
            return new MasterCodeCache(true);
        } else {
            return new MasterCodeCache(false);
        }
    }

    private MasterCodeCache(boolean init) {
        if (init) {
            init();
        }
    }

    @Override
    public void init() {
        idCache.invalidateAll();
        typeCache.invalidateAll();
        MasterCodeService service = SpringUtils.getBean(MasterCodeService.class);
        List<MasterCode> masterCodeList = service.selectAll();
        for (MasterCode masterCode : masterCodeList) {
            idCache.put(masterCode.getId(), masterCode);
            updateTypeCache(masterCode);
        }
    }

    @Override
    public MasterCode get(Integer key) {
        return BeanUtils.deepClone(idCache.getIfPresent(key));
    }

    public MasterCode getByCodeTypeAndCode(String codeType, String code) {
        List<MasterCode> list = typeCache.getIfPresent(codeType);
        if (list != null) {
            for (MasterCode masterCode : list) {
                if (masterCode.getCode().equals(codeType)) {
                    return BeanUtils.deepClone(masterCode);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Integer key, MasterCode value) {
        if (NumberUtils.isPositive(key) && value != null) {
            idCache.put(key, value);
            updateTypeCache(value);
        }
    }

    @Override
    public void remove(Integer key) {
        MasterCode masterCode = idCache.getIfPresent(key);
        if (masterCode != null) {
            idCache.invalidate(key);
            List<MasterCode> list = typeCache.getIfPresent(masterCode.getCodeType());
            if (list != null) {
                list.removeIf(current -> current.getId().equals(key));
                if (list.size() == 0) {
                    typeCache.invalidate(masterCode.getCodeType());
                }
            }
        }
    }

    private void updateTypeCache(MasterCode masterCode) {
        List<MasterCode> list = typeCache.getIfPresent(masterCode.getCodeType());
        if (list == null) {
            list = new ArrayList<>();
            list.add(masterCode);
            typeCache.put(masterCode.getCodeType(), list);
        } else {
            list.add(masterCode);
        }
    }
}
