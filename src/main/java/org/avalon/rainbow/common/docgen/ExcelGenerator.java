package org.avalon.rainbow.common.docgen;

import org.apache.commons.io.FilenameUtils;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public abstract class ExcelGenerator implements IDocGenerator {

    private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

    @Override
    public byte[] generate() throws Exception {
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        try (FileInputStream inputStream = getTemplateAsStream(getTemplate())) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Transformer transformer = jxlsHelper.createTransformer(inputStream, outputStream);
            Context context = new Context();
            if (getParameters() != null) {
                getParameters().forEach(context::putVar);
            }
            jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    private FileInputStream getTemplateAsStream(String path) throws Exception {
        if ("xlsx".equalsIgnoreCase(FilenameUtils.getExtension(path))) {
            String classPath = this.getClass().getResource("/").getPath().replace("%20", " ") + "templates/";
            File file = new File(classPath + path);
            if (file.isFile() && file.exists()) {
                return new FileInputStream(file);
            }
        }
        throw new FileNotFoundException();
    }

}
