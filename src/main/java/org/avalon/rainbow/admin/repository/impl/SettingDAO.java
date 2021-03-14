package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Setting;
import org.avalon.rainbow.admin.repository.SettingRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SettingDAO extends CrudDAO<Setting, Long, SettingRepository> {

    @Autowired
    protected SettingDAO(SettingRepository crudRepository) {
        super(crudRepository);
    }

    public Setting findBySettingKey(String settingKey) {
        return crudRepository.findBySettingKey(settingKey);
    }
}
