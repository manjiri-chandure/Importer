package com.importer.importer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.importer.importer.repository")
@EnableTransactionManagement
public class ImporterApplication {

	public static void main(String[] args) {
//		SpringApplication application = new SpringApplication(ImporterApplication.class);
//		application.addListeners(new PropertiesListener());
//		application.run(args);
		SpringApplication.run(ImporterApplication.class, args);

	}

}
