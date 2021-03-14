package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.RoleAssignment;
import org.avalon.rainbow.admin.repository.RoleAssignmentRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RoleAssignmentDAO extends CrudDAO<RoleAssignment, Long, RoleAssignmentRepository> {

    protected RoleAssignmentDAO(RoleAssignmentRepository crudRepository) {
        super(crudRepository);
    }

    public List<RoleAssignment> findByUserIdAndStatus(Long userId, String status) {
         return crudRepository.findByUserIdAndStatus(userId, status);
    }
}
