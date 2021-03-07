package org.avalon.rainbow.admin.service;

import org.avalon.rainbow.admin.entity.Template;
import org.avalon.rainbow.admin.repository.impl.TemplateDAO;
import org.avalon.rainbow.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@Service
public class TemplateService {

    private final TemplateDAO templateDAO;
    private static final Logger log = LoggerFactory.getLogger(TemplateDAO.class);

    @Autowired
    public TemplateService(TemplateDAO templateDAO) {
        this.templateDAO = templateDAO;
    }

    public Template getTemplateByPath(String path) {
        return templateDAO.findByPath(path);
    }

    public String getActualTemplatePath(String path) {
        Template template = getTemplateByPath(path);
        if (template != null) {
            if (StringUtils.isEmpty(template.getModifiedContent())) {
                return template.getPath();
            } else {
                String fullPath = getTempFileFullPath(path);
                File file = new File(fullPath + ".html");
                if (file.exists() && file.isFile()) {
                    log.info("Modified template exists");
                    return template.getPath() + "_Modified";
                }
                log.info("Modified template has not created, start creation");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath + ".html"))) {
                    writer.write(template.getModifiedContent());
                    return template.getPath() + "_Modified";
                } catch (Exception e) {
                    log.error("", e);
                    return template.getPath();
                }
            }
        }
        return null;
    }

    private String getTempFileFullPath(String path) {
        if (StringUtils.isNotBlank(path)) {
            String classpath = this.getClass().getResource("/").getPath().replace("%20", " ");
            return classpath + "templates/" + path + "_Modified";
        }
        return null;
    }


}
