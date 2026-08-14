package org.no23sports.calorietrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CalorieTrackingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalorieTrackingServiceApplication.class, args);
	}

}
