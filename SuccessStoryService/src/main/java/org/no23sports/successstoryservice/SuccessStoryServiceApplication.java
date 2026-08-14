package org.no23sports.successstoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SuccessStoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuccessStoryServiceApplication.class, args);
	}

}
