package com.compilou.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.compilou.regex.repositories")
public class RegexApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegexApplication.class, args);
	}
}
