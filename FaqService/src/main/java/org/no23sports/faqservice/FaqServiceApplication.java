package org.no23sports.faqservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FaqServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaqServiceApplication.class, args);
	}

}
