package org.avalon.rainbow.admin.repository.impl;

import org.apache.commons.collections4.IterableUtils;
import org.avalon.rainbow.admin.repository.MasterCodeRepository;
import org.avalon.rainbow.admin.entity.MasterCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MasterCodeDAO {

    private final MasterCodeRepository masterCodeRepository;

    @Autowired
    public MasterCodeDAO(MasterCodeRepository masterCodeRepository) {
        this.masterCodeRepository = masterCodeRepository;
    }

    public List<MasterCode> findAll() {
        return IterableUtils.toList(masterCodeRepository.findAll());
    }

    public MasterCode save(MasterCode masterCode) {
        return masterCodeRepository.save(masterCode);
    }

}
