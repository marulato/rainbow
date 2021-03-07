package org.avalon.rainbow.admin.repository;

import org.avalon.rainbow.admin.entity.Template;
import org.springframework.data.repository.CrudRepository;

public interface TemplateRepository extends CrudRepository<Template, Long> {

    Template findByPath(String path);
}
