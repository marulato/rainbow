package org.avalon.rainbow.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.avalon.rainbow.admin.entity.MasterCode;
import org.avalon.rainbow.admin.repository.impl.MasterCodeDAO;
import org.avalon.rainbow.common.utils.BeanUtils;
import org.avalon.rainbow.common.utils.NumberUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@DependsOn({"beanUtils", "masterCodeDAO"})
public class MasterCodeCache implements ICache<Long, MasterCode> {

    private static final Cache<Long, MasterCode> idCache = CacheBuilder.newBuilder().build();
    private static final Cache<String, List<MasterCode>> typeCache = CacheBuilder.newBuilder().build();


    private MasterCodeCache() {
        init();
    }

    @Override
    public void init() {
        idCache.invalidateAll();
        typeCache.invalidateAll();
        MasterCodeDAO dao = BeanUtils.getBean(MasterCodeDAO.class);
        List<MasterCode> masterCodeList = dao.findAll();
        for (MasterCode masterCode : masterCodeList) {
            idCache.put(masterCode.getId(), masterCode);
            updateTypeCache(masterCode);
        }
    }

    @Override
    public MasterCode get(Long key) {
        return BeanUtils.deepClone(idCache.getIfPresent(key));
    }

    public List<MasterCode> get(String type) {
         List<MasterCode> masterCodeList = typeCache.getIfPresent(type);
         List<MasterCode> cloneList = new ArrayList<>();
         if (masterCodeList != null) {
             for (MasterCode masterCode : masterCodeList) {
                cloneList.add(BeanUtils.deepClone(masterCode));
             }
         }
         return cloneList;
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
    public void update(Long key, MasterCode value) {
        if (NumberUtils.isPositive(key) && value != null) {
            idCache.put(key, value);
            updateTypeCache(value);
        }
    }

    @Override
    public void remove(Long key) {
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
