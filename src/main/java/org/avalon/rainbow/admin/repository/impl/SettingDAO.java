package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Setting;
import org.avalon.rainbow.admin.repository.SettingRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class SettingDAO extends CrudDAO<Setting, Long, SettingRepository> {


    protected SettingDAO(SettingRepository crudRepository) {
        super(crudRepository);
    }
}
