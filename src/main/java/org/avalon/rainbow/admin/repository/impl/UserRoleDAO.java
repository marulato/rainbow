package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.UserRole;
import org.avalon.rainbow.admin.repository.UserRoleRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class UserRoleDAO extends CrudDAO<UserRole, Long, UserRoleRepository> {

    protected UserRoleDAO(UserRoleRepository crudRepository) {
        super(crudRepository);
    }

    public UserRole findByCode(String roleId) {
        return crudRepository.findByCode(roleId);
    }

}
