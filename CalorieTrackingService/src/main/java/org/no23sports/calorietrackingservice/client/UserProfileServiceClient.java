package org.no23sports.calorietrackingservice.client;

import java.util.UUID;

import org.no23sports.calorietrackingservice.exception.UserProfileServiceUnavailableException;
import org.no23sports.calorietrackingservice.model.NutritionGoalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

// The tracking panel's "Bugünkü hedefi" line reuses UserProfileService's
// existing BMR/TDEE/macro calculator (see Specs.md section 4/7) instead of
// duplicating that math here - one calorie-calculation algorithm, referenced
// by both the "Kalori Hesaplama" page and this daily tracking summary.
@Component
public class UserProfileServiceClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${user-profile-service.name:USER-PROFILE-SERVICE}")
	private String userProfileServiceName;

	public NutritionGoalDto getNutritionGoal(UUID userId) {
		String url = "http://" + userProfileServiceName + "/profile/nutrition/user/" + userId;
		try {
			return restTemplate.exchange(url, HttpMethod.GET, forwardedAuthEntity(), NutritionGoalDto.class).getBody();
		} catch (RestClientException e) {
			throw new UserProfileServiceUnavailableException(
					"Could not reach user-profile-service to fetch the nutrition goal for user " + userId + ".", e);
		}
	}

	// user-profile-service's JwtAuthFilter requires a Bearer token on every
	// request, so the incoming user's token is forwarded rather than
	// introducing a separate service-to-service credential.
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
