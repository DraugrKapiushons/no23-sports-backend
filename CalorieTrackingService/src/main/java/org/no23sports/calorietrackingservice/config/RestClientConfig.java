package org.no23sports.calorietrackingservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

	// @LoadBalanced resolves the "http://MENU-SERVICE/..." host in
	// MenuServiceClient against Eureka instead of DNS, and spreads calls
	// across every registered menu-service instance — this is exactly the
	// mechanism that lets menu-service scale independently under load.
	@Bean
	@LoadBalanced
	public RestTemplate loadBalancedRestTemplate() {
		return new RestTemplate();
	}
}
