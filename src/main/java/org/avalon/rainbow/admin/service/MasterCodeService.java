package org.avalon.rainbow.admin.service;

import org.avalon.rainbow.admin.dao.MasterCodeDAO;
import org.avalon.rainbow.admin.entity.MasterCode;
import org.avalon.rainbow.common.jpa.exec.BaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MasterCodeService {

    private final MasterCodeDAO masterCodeDAO;

    @Autowired
    public MasterCodeService(MasterCodeDAO masterCodeDAO) {
        this.masterCodeDAO = masterCodeDAO;
    }

    public List<MasterCode> selectAll() {
        return masterCodeDAO.selectAll();
    }

    public void create(MasterCode masterCode) {
        BaseDAO.save(masterCode);
    }

}
