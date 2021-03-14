package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.RoleAccess;
import org.avalon.rainbow.admin.repository.RoleAccessRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RoleAccessDAO extends CrudDAO<RoleAccess, Long, RoleAccessRepository> {

    protected RoleAccessDAO(RoleAccessRepository crudRepository) {
        super(crudRepository);
    }

    public List<RoleAccess> findByRoleId(Long roleId) {
        return crudRepository.findByRoleId(roleId);
    }
}
