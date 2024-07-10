package com.importer.importer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import java.util.Properties;

@Configuration
public class PropertyConfig {

    @Autowired
    private ConfigurableEnvironment env;

    @Value("${database.encrypted-password}")
    private String encryptedDatabasePassword;

    @Autowired
    private KMSUtil kmsUtil;

    @PostConstruct
    public void setProperty() {
        Properties props = new Properties();
        props.put("spring.datasource.password", kmsUtil.kmsDecrypt(encryptedDatabasePassword));
       env.getPropertySources().addFirst(new PropertiesPropertySource("customProperty", props));
   }

}
