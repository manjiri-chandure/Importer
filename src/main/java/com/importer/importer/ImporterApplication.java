package com.importer.importer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.importer.importer.repository")
@EnableTransactionManagement
public class ImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImporterApplication.class, args);
	}

}
