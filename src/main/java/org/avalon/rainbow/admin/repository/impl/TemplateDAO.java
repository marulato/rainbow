package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Template;
import org.avalon.rainbow.admin.repository.TemplateRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateDAO extends CrudDAO<Template, Long, TemplateRepository> {

    private final TemplateRepository repository;

    protected TemplateDAO(TemplateRepository crudRepository) {
        super(crudRepository);
        this.repository = crudRepository;
    }

    public Template findByPath(String path) {
        return repository.findByPath(path);
    }
}
