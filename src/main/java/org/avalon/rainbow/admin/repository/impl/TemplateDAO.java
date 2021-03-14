package org.avalon.rainbow.admin.repository.impl;

import org.avalon.rainbow.admin.entity.Template;
import org.avalon.rainbow.admin.repository.TemplateRepository;
import org.avalon.rainbow.common.jpa.CrudDAO;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateDAO extends CrudDAO<Template, Long, TemplateRepository> {


    protected TemplateDAO(TemplateRepository crudRepository) {
        super(crudRepository);
    }

    public Template findByPath(String path) {
        return crudRepository.findByPath(path);
    }
}
