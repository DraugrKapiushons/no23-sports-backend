package org.no23sports.mealplanservice.client;

import java.util.List;

import org.no23sports.mealplanservice.exception.MenuServiceUnavailableException;
import org.no23sports.mealplanservice.model.MenuItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

// The one piece of network coupling this split introduces: generating a plan
// still needs the full menu catalog, so this calls menu-service over HTTP
// instead of hitting KitchenMenuItemRepo directly. Everything else about
// mealplan-service (its own database, its own deploy, its own scaling) is
// fully independent of menu-service.
@Component
public class MenuServiceClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${menu-service.name:MENU-SERVICE}")
	private String menuServiceName;

	public List<MenuItemDto> getAllMenuItems() {
		String url = "http://" + menuServiceName + "/kitchen/menu";
		try {
			MenuItemDto[] items = restTemplate.exchange(url, HttpMethod.GET, forwardedAuthEntity(), MenuItemDto[].class)
					.getBody();
			return items == null ? List.of() : List.of(items);
		} catch (RestClientException e) {
			throw new MenuServiceUnavailableException("Could not reach menu-service to fetch menu items.", e);
		}
	}

	// Menu-service's JwtAuthFilter requires a Bearer token on every request,
	// so the incoming user's token is forwarded on this internal call rather
	// than introducing a separate service-to-service credential.
	private HttpEntity<Void> forwardedAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs != null) {
			HttpServletRequest request = attrs.getRequest();
			String authHeader = request.getHeader("Authorization");
			if (authHeader != null) {
				headers.set("Authorization", authHeader);
			}
		}
		return new HttpEntity<>(headers);
	}
}
