package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.RoleAssignment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleAssignmentRepository extends CrudRepository<RoleAssignment, Long> {

    List<RoleAssignment> findByUserIdAndStatus(Long userId, String status);
}
