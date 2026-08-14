package org.no23sports.paymentservice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
	@Bean
	public FilterRegistrationBean<JwtAuthFilter> jwtFilterRegistration(JwtAuthFilter filter) {
	    FilterRegistrationBean<JwtAuthFilter> reg = new FilterRegistrationBean<>(filter);
	    reg.addUrlPatterns("/*");
	    reg.setOrder(1);
	    return reg;
	}
}
