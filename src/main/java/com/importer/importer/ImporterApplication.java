package com.importer.importer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
		info = @Info(
				title = "importer",
				version = "1.0.0",
				description = "This project is Importer project which is used to import students",
				termsOfService = "Copyright@2023",
				contact = @Contact(
						name = "Manjiri Chandure",
						email = "chanduremanjiri@gmail.com"
				)
		)
)
@SpringBootApplication
public class ImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImporterApplication.class, args);
	}

}
