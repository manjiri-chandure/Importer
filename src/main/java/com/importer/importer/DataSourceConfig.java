package com.importer.importer;
import com.importer.importer.service.SecretsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DataSourceConfig {

    @Autowired
    private SecretsManagerService secretsManagerService;

    @Value("${spring.datasource.url}")
    private String dataBaseUrl;

    @Value("${spring.datasource.username}")
    private String dataBaseUsername;

    @Bean
    public DataSource dataSource() throws IOException {
        String databasePassword = secretsManagerService.getSecret("intern_manjiri_jwt_secret","db_password");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dataBaseUrl);
        dataSource.setUsername(dataBaseUsername);
        dataSource.setPassword(databasePassword);

        return dataSource;
    }

}
