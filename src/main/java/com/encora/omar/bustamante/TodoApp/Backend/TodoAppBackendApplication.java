package com.encora.omar.bustamante.TodoApp.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TodoAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppBackendApplication.class, args);
	}

}
