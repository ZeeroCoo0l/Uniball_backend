package com.demo.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.demo.demo.repos")
public class UniballApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniballApplication.class, args);
	}

}