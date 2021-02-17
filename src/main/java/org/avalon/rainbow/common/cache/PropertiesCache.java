package org.avalon.rainbow.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Component
public class PropertiesCache implements ICache<String, String> {

    private static final Properties mainProperties = new Properties(75);
    private static final Properties envProperties = new Properties(75);
    private static final Logger log = LoggerFactory.getLogger(PropertiesCache.class);

    private PropertiesCache() {
        init();
    }

    @Override
    public void init() {
        File classPath = new File(PropertiesCache.class.getResource("/").getPath().replace("%20", " "));
        File mainPropertiesFile = new File(classPath, "rainbow.properties");
        loadProperties(mainPropertiesFile, mainProperties);

        String env = mainProperties.getProperty("server.environment");
        File envPropertiesFile = new File(classPath, env + ".properties");
        loadProperties(envPropertiesFile, envProperties);

    }

    @Override
    public String get(String key) {
        String value = mainProperties.getProperty(key);
        if (value == null) {
            value = envProperties.getProperty(key);
        }
        return value;
    }

    @Override
    public void update(String key, String value) {

    }

    @Override
    public void remove(String key) {

    }

    private void loadProperties(File file, Properties properties) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        if (file != null && file.isFile() && file.exists()) {
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                properties.load(bufferedReader);
            } catch (Exception e) {
                log.error("", e);
            } finally {
                close(fileReader, bufferedReader);
            }
        }
    }

    private void close(FileReader fileReader, BufferedReader bufferedReader) {
        if (fileReader != null) {
            try {
                fileReader.close();
            } catch (IOException e1) {
                log.error("", e1);
            }
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e2) {
                log.error("", e2);
            }
        }
    }
}
