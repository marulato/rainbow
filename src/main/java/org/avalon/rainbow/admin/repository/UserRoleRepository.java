package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

    UserRole findByCode(String roleId);
}
