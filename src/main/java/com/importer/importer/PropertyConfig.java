package com.importer.importer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final String encryptionExpression = "^ENC\\(.*\\)$";

    private static final Pattern encryptionPattern = Pattern.compile(encryptionExpression);

    @PostConstruct
    public void setProperties() {

        databasePassword = decryptIfPropertyIsEncrypted(databasePassword);
        jwtSecret = decryptIfPropertyIsEncrypted(jwtSecret);

        Properties props = new Properties();
        props.put("spring.datasource.password", databasePassword);
        props.put("jwt.secret",jwtSecret);
        env.getPropertySources().addFirst(new PropertiesPropertySource("customProperty", props));
   }

    private String decryptIfPropertyIsEncrypted(String property) {

        if(encryptionPattern.matcher(property).matches()){
            String encryptedProperty = property.substring(4, property.length()-1);//last character ')' get excluded
            return kmsUtil.kmsDecrypt(encryptedProperty);
        }
        return property;
    }

}
