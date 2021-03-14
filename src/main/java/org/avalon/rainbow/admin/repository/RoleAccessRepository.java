package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.RoleAccess;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleAccessRepository extends CrudRepository<RoleAccess, Long> {

    List<RoleAccess> findByRoleId(Long roleId);
}
