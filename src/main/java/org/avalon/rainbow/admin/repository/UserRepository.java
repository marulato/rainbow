package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
