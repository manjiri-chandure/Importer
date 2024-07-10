package com.importer.importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() throws IOException {
        String dbPassword = environment.getProperty("spring.datasource.password");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dataBaseUrl);
        dataSource.setUsername(dataBaseUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }
}
