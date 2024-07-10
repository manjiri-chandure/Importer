package com.importer.importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
//@DependsOn("propertyConfig")
public class DataSourceConfig {


    @Value("${spring.datasource.url}")
    private String dataBaseUrl;

    @Value("${spring.datasource.username}")
    private String dataBaseUsername;
    
    @Value("${spring.datasource.password}")
    private String dataBasePassword;

    @Autowired
    private KMSUtil kmsUtil;

    @Bean
    public DataSource dataSource() throws IOException {
       // String dbPassword = environment.getProperty("spring.datasource.password");
        boolean isEncrypted = checkDbPasswordIsEncrypted(dataBasePassword);
        if(isEncrypted){
          dataBasePassword = decryptEncryptedPassword(dataBasePassword);
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dataBaseUrl);
        dataSource.setUsername(dataBaseUsername);
        dataSource.setPassword(dataBasePassword);
        return dataSource;
    }

    private boolean checkDbPasswordIsEncrypted(String dataBasePassword) {
        return dataBasePassword.startsWith("ENC(") & dataBasePassword.endsWith(")");
    }

    private String decryptEncryptedPassword(String dataBasePassword) {
        String encryptedPassword = dataBasePassword.substring(4,dataBasePassword.length()-1);//last character ')' get excluded
        return kmsUtil.kmsDecrypt(encryptedPassword);
    }

}
