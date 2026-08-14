package org.no23sports.reservationservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	private static final String BEARER_SCHEME = "bearerAuth";

	@Bean
	public OpenAPI reservationServiceOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("ReservationService API")
						.description("Group-lesson and Personal Training booking for the NO23 Sports Club "
								+ "platform - the \"Randevu Al\" homepage flow and the member panel's "
								+ "\"Yaklaşan rezervasyonlar\" list.")
						.version("0.0.1-SNAPSHOT"))
				.addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
				.components(new Components()
						.addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
								.name(BEARER_SCHEME)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")));
	}
}
