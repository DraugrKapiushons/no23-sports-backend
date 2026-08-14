package org.no23sports.calorietrackingservice.client;

import org.no23sports.calorietrackingservice.exception.MenuItemNotFoundException;
import org.no23sports.calorietrackingservice.exception.MenuServiceUnavailableException;
import org.no23sports.calorietrackingservice.model.MenuItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

// Logging a meal "from the menu" (menuItemId set on CreateFoodLogRequest)
// needs that item's macros, so this calls menu-service over HTTP instead of
// duplicating KitchenMenuItemRepo here. Same loose-coupling convention
// MealPlanService's own MenuServiceClient already uses.
@Component
public class MenuServiceClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${menu-service.name:MENU-SERVICE}")
	private String menuServiceName;

	public MenuItemDto getMenuItem(int menuItemId) {
		String url = "http://" + menuServiceName + "/kitchen/menu/" + menuItemId;
		try {
			return restTemplate.exchange(url, HttpMethod.GET, forwardedAuthEntity(), MenuItemDto.class).getBody();
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new MenuItemNotFoundException(menuItemId);
			}
			throw new MenuServiceUnavailableException("Could not reach menu-service to fetch menu item " + menuItemId + ".", e);
		} catch (RestClientException e) {
			throw new MenuServiceUnavailableException("Could not reach menu-service to fetch menu item " + menuItemId + ".", e);
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
