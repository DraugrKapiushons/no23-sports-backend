package org.no23sports.userprofileservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.no23sports.userprofileservice.exception.UserNotFoundException;
import org.no23sports.userprofileservice.model.ActivityLevel;
import org.no23sports.userprofileservice.model.Goal;
import org.no23sports.userprofileservice.model.Response;
import org.no23sports.userprofileservice.model.UserProfile;
import org.no23sports.userprofileservice.repository.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
	
	@Autowired
	private UserProfileRepo repo;

	public UserProfile getUserDetails(int id) {
		return repo.findById(id).orElseThrow(
				() -> new UserNotFoundException(id));
	}
	
	public BigDecimal calculateBMR(UserProfile profile) {
	    BigDecimal weightTerm = profile.getWeight().multiply(BigDecimal.valueOf(10));
	    BigDecimal heightTerm = profile.getHeight().multiply(BigDecimal.valueOf(6.25));
	    BigDecimal ageTerm = BigDecimal.valueOf(profile.getAge()).multiply(BigDecimal.valueOf(5));

	    BigDecimal base = weightTerm.add(heightTerm).subtract(ageTerm);

	    return switch (profile.getGender()) {
	        case MALE -> base.add(BigDecimal.valueOf(5));
	        case FEMALE -> base.subtract(BigDecimal.valueOf(161));
	    };
	}
	
	public BigDecimal calculateTDEE(ActivityLevel level, BigDecimal bmr) {
		return switch (level){
			case SEDENTARY -> bmr.multiply(BigDecimal.valueOf(1.2));
			case LIGHT -> bmr.multiply(BigDecimal.valueOf(1.375));
			case MODERATE -> bmr.multiply(BigDecimal.valueOf(1.55));
			case ACTIVE -> bmr.multiply(BigDecimal.valueOf(1.725));
			case VERY_ACTIVE -> bmr.multiply(BigDecimal.valueOf(1.9));
		};
	}
	
	public BigDecimal calculateTargetCalories(BigDecimal tdee, Goal goal) {
	    return switch (goal) {
	        case MUSCLE_GAIN -> tdee.add(BigDecimal.valueOf(400));
	        case LOSE_BODY_FAT -> tdee.subtract(BigDecimal.valueOf(500));
	        case MAINTAIN_BODY_WEIGHT -> tdee;
	        case HEALTHY_LIFESTYLE -> tdee.subtract(BigDecimal.valueOf(100));
	        case PERFORMANCE_NUTRITION -> tdee.add(BigDecimal.valueOf(100));
	    };
	}
	
	public Response calculateNutrition(UserProfile profile) {
	    BigDecimal bmr = calculateBMR(profile);
	    BigDecimal tdee = calculateTDEE(profile.getActivityLevel(), bmr);
	    BigDecimal targetCalories = calculateTargetCalories(tdee, profile.getGoal());

	    MacroFactors factors = getMacroFactors(profile.getGoal());
	    BigDecimal weight = profile.getWeight();

	    BigDecimal proteinGrams = weight.multiply(factors.proteinPerKg());
	    BigDecimal fatGrams = weight.multiply(factors.fatPerKg());

	    BigDecimal proteinCalories = proteinGrams.multiply(BigDecimal.valueOf(4));
	    BigDecimal fatCalories = fatGrams.multiply(BigDecimal.valueOf(9));

	    BigDecimal carbCalories = targetCalories.subtract(proteinCalories).subtract(fatCalories);
	    BigDecimal carbGrams = carbCalories.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);

	    return new Response(
	        targetCalories.setScale(2, RoundingMode.HALF_UP),
	        proteinGrams.setScale(2, RoundingMode.HALF_UP),
	        carbGrams,
	        fatGrams.setScale(2, RoundingMode.HALF_UP)
	    );
	}
	
	private MacroFactors getMacroFactors(Goal goal) {
	    return switch (goal) {
	        case MUSCLE_GAIN -> new MacroFactors(BigDecimal.valueOf(2.1), BigDecimal.valueOf(0.9));
	        case LOSE_BODY_FAT -> new MacroFactors(BigDecimal.valueOf(2.2), BigDecimal.valueOf(0.8));
	        case MAINTAIN_BODY_WEIGHT -> new MacroFactors(BigDecimal.valueOf(1.6), BigDecimal.valueOf(1.0));
	        case HEALTHY_LIFESTYLE -> new MacroFactors(BigDecimal.valueOf(1.5), BigDecimal.valueOf(1.0));
	        case PERFORMANCE_NUTRITION -> new MacroFactors(BigDecimal.valueOf(1.9), BigDecimal.valueOf(1.0));
	    };
	}

	private record MacroFactors(BigDecimal proteinPerKg, BigDecimal fatPerKg) {}

	public Response getUserNutrition(int id) {
		UserProfile profile = repo.findById(id).orElseThrow(
				() -> new UserNotFoundException(id));
		return calculateNutrition(profile);
	}

	// UUID-keyed variants: other services (ReservationService, MealPlanService,
	// CalorieTrackingService, ...) all identify a member by AuthService's
	// User.uuid, not this service's own internal integer PK, so a lookup by
	// that external id is needed for cross-service calls like
	// CalorieTrackingService's daily goal line.
	public UserProfile getUserDetailsByUserId(UUID userId) {
		return repo.findByUserId(userId).orElseThrow(
				() -> new UserNotFoundException(userId));
	}

	public Response getUserNutritionByUserId(UUID userId) {
		UserProfile profile = repo.findByUserId(userId).orElseThrow(
				() -> new UserNotFoundException(userId));
		return calculateNutrition(profile);
	}

}
