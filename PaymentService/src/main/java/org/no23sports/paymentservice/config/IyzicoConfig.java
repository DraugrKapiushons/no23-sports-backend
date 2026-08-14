package org.no23sports.paymentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.iyzipay.Options;

@Configuration
public class IyzicoConfig {

	@Value("${iyzico.api-key}")
	private String apiKey;

	@Value("${iyzico.secret-key}")
	private String secretKey;

	// https://sandbox-api.iyzipay.com while testing, https://api.iyzipay.com in production.
	@Value("${iyzico.base-url}")
	private String baseUrl;

	@Bean
	public Options iyzicoOptions() {
		Options options = new Options();
		options.setApiKey(apiKey);
		options.setSecretKey(secretKey);
		options.setBaseUrl(baseUrl);
		return options;
	}
}
