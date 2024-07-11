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

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private KMSUtil kmsUtil;

    @PostConstruct
    public void setProperties() {

        if(checkPropertyIsEncrypted(databasePassword)){
            databasePassword = decryptEncryptedProperty(databasePassword);
        }

        if(checkPropertyIsEncrypted(jwtSecret)){
            jwtSecret = decryptEncryptedProperty(jwtSecret);
        }

        Properties props = new Properties();
        //properties get replace here
        props.put("spring.datasource.password", databasePassword);
        props.put("jwt.secret",jwtSecret);
        env.getPropertySources().addFirst(new PropertiesPropertySource("customProperty", props));
   }

    private boolean checkPropertyIsEncrypted(String property) {
        return property.startsWith("ENC(") & property.endsWith(")");
    }

    private String decryptEncryptedProperty(String property) {
        String encryptedProperty = property.substring(4, property.length()-1);//last character ')' get excluded
        return kmsUtil.kmsDecrypt(encryptedProperty);
    }

}
