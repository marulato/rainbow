package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.User;
import org.avalon.rainbow.admin.repository.UserRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends CrudDAO<User, Long, UserRepository> {

    private final UserRepository repository;

    protected UserDAO(UserRepository crudRepository) {
        super(crudRepository);
        this.repository = crudRepository;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
