package org.no23sports.authservice;


import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthServiceApp {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AuthServiceApp.class, args);
	}
}
