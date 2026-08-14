package org.no23sports.calorietrackingservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.calorietrackingservice.client.MenuServiceClient;
import org.no23sports.calorietrackingservice.client.UserProfileServiceClient;
import org.no23sports.calorietrackingservice.exception.FoodLogEntryNotFoundException;
import org.no23sports.calorietrackingservice.exception.UserProfileServiceUnavailableException;
import org.no23sports.calorietrackingservice.exception.WaterLogEntryNotFoundException;
import org.no23sports.calorietrackingservice.model.CreateFoodLogRequest;
import org.no23sports.calorietrackingservice.model.CreateWaterLogRequest;
import org.no23sports.calorietrackingservice.model.DailySummaryResponse;
import org.no23sports.calorietrackingservice.model.FoodLogEntry;
import org.no23sports.calorietrackingservice.model.MenuItemDto;
import org.no23sports.calorietrackingservice.model.NutritionGoalDto;
import org.no23sports.calorietrackingservice.model.WaterLogEntry;
import org.no23sports.calorietrackingservice.repository.FoodLogEntryRepo;
import org.no23sports.calorietrackingservice.repository.WaterLogEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalorieTrackingService {

	@Autowired
	private FoodLogEntryRepo foodLogRepo;
	@Autowired
	private WaterLogEntryRepo waterLogRepo;
	@Autowired
	private MenuServiceClient menuServiceClient;
	@Autowired
	private UserProfileServiceClient userProfileServiceClient;

	// Default daily water target (roughly the commonly-cited 8x250ml
	// glasses) used whenever no goal is configured elsewhere. There's no
	// per-member water target field in UserProfileService today, unlike
	// calories/macros, so this is a flat constant rather than a lookup.
	@Value("${tracking.default-water-goal-ml:2000}")
	private int defaultWaterGoalMl;

	// --- Food log ---------------------------------------------------

	public FoodLogEntry logFood(CreateFoodLogRequest request) {
		if (request.getUserId() == null) {
			throw new IllegalArgumentException("userId is required.");
		}
		if (request.getMealType() == null) {
			throw new IllegalArgumentException("mealType is required.");
		}
		LocalDate logDate = request.getLogDate() != null ? request.getLogDate() : LocalDate.now();
		BigDecimal quantity = request.getQuantity() != null ? request.getQuantity() : BigDecimal.ONE;

		String name;
		BigDecimal calories;
		BigDecimal protein;
		BigDecimal carbs;
		BigDecimal fat;

		if (request.getMenuItemId() != null) {
			// Logged straight from the NO23 Kitchen menu - fetch the item's
			// macros from menu-service and scale by quantity. The scaled
			// values are copied into this row (not re-derived on every
			// read) so a later edit to the menu item's calories doesn't
			// rewrite the member's past history.
			MenuItemDto item = menuServiceClient.getMenuItem(request.getMenuItemId());
			name = request.getName() != null ? request.getName() : item.getName();
			calories = scale(item.getCalories(), quantity);
			protein = scale(item.getProtein(), quantity);
			carbs = scale(item.getCarbs(), quantity);
			fat = scale(item.getFat(), quantity);
		} else {
			// Free-text custom entry - the member supplies both the name
			// and the macros directly (e.g. a home-cooked meal not on the
			// NO23 Kitchen menu).
			if (request.getName() == null || request.getName().isBlank()) {
				throw new IllegalArgumentException("name is required when menuItemId is not provided.");
			}
			name = request.getName();
			calories = orZero(request.getCalories());
			protein = orZero(request.getProtein());
			carbs = orZero(request.getCarbs());
			fat = orZero(request.getFat());
		}

		FoodLogEntry entry = new FoodLogEntry(request.getUserId(), logDate, request.getMealType(),
				request.getMenuItemId(), name, quantity, calories, protein, carbs, fat);
		return foodLogRepo.save(entry);
	}

	public List<FoodLogEntry> getFoodLogForDate(UUID userId, LocalDate date) {
		return foodLogRepo.findByUserIdAndLogDateOrderByLoggedAtAsc(userId, date);
	}

	public void deleteFoodLogEntry(int id) {
		if (!foodLogRepo.existsById(id)) {
			throw new FoodLogEntryNotFoundException(id);
		}
		foodLogRepo.deleteById(id);
	}

	// --- Water log ---------------------------------------------------

	public WaterLogEntry logWater(CreateWaterLogRequest request) {
		if (request.getUserId() == null) {
			throw new IllegalArgumentException("userId is required.");
		}
		if (request.getAmountMl() <= 0) {
			throw new IllegalArgumentException("amountMl must be greater than zero.");
		}
		LocalDate logDate = request.getLogDate() != null ? request.getLogDate() : LocalDate.now();
		WaterLogEntry entry = new WaterLogEntry(request.getUserId(), logDate, request.getAmountMl());
		return waterLogRepo.save(entry);
	}

	public List<WaterLogEntry> getWaterLogForDate(UUID userId, LocalDate date) {
		return waterLogRepo.findByUserIdAndLogDateOrderByLoggedAtAsc(userId, date);
	}

	public void deleteWaterLogEntry(int id) {
		if (!waterLogRepo.existsById(id)) {
			throw new WaterLogEntryNotFoundException(id);
		}
		waterLogRepo.deleteById(id);
	}

	// --- Daily summary -------------------------------------------------

	public DailySummaryResponse getDailySummary(UUID userId, LocalDate date) {
		List<FoodLogEntry> meals = getFoodLogForDate(userId, date);
		List<WaterLogEntry> waterEntries = getWaterLogForDate(userId, date);

		BigDecimal consumedCalories = sum(meals, FoodLogEntry::getCalories);
		BigDecimal consumedProtein = sum(meals, FoodLogEntry::getProtein);
		BigDecimal consumedCarbs = sum(meals, FoodLogEntry::getCarbs);
		BigDecimal consumedFat = sum(meals, FoodLogEntry::getFat);
		int waterConsumedMl = waterEntries.stream().mapToInt(WaterLogEntry::getAmountMl).sum();

		DailySummaryResponse response = new DailySummaryResponse();
		response.setDate(date);
		response.setMeals(meals);
		response.setWaterEntries(waterEntries);
		response.setConsumedCalories(consumedCalories);
		response.setConsumedProtein(consumedProtein);
		response.setConsumedCarbs(consumedCarbs);
		response.setConsumedFat(consumedFat);
		response.setWaterConsumedMl(waterConsumedMl);
		response.setWaterGoalMl(defaultWaterGoalMl);

		// The goal line degrades gracefully rather than failing the whole
		// summary: a member who hasn't filled in their UserProfileService
		// profile yet (or a momentary hiccup reaching that service) still
		// gets to see what they've logged today, just without a target to
		// compare against.
		BigDecimal goalCalories = BigDecimal.ZERO;
		BigDecimal goalProtein = BigDecimal.ZERO;
		BigDecimal goalCarbs = BigDecimal.ZERO;
		BigDecimal goalFat = BigDecimal.ZERO;
		try {
			NutritionGoalDto goal = userProfileServiceClient.getNutritionGoal(userId);
			if (goal != null) {
				goalCalories = orZero(goal.getDailyCalories());
				goalProtein = orZero(goal.getProtein());
				goalCarbs = orZero(goal.getCarbohidrates());
				goalFat = orZero(goal.getFats());
			}
		} catch (UserProfileServiceUnavailableException e) {
			// Goals stay at zero; consumed/remaining totals below still
			// reflect what was actually logged.
		}

		response.setGoalCalories(goalCalories);
		response.setGoalProtein(goalProtein);
		response.setGoalCarbs(goalCarbs);
		response.setGoalFat(goalFat);

		response.setRemainingCalories(goalCalories.subtract(consumedCalories));
		response.setRemainingProtein(goalProtein.subtract(consumedProtein));
		response.setRemainingCarbs(goalCarbs.subtract(consumedCarbs));
		response.setRemainingFat(goalFat.subtract(consumedFat));

		return response;
	}

	public List<DailySummaryResponse> getHistory(UUID userId, LocalDate from, LocalDate to) {
		if (from.isAfter(to)) {
			throw new IllegalArgumentException("from must not be after to.");
		}
		return from.datesUntil(to.plusDays(1)).map(date -> getDailySummary(userId, date)).toList();
	}

	private BigDecimal scale(BigDecimal value, BigDecimal quantity) {
		return orZero(value).multiply(quantity).setScale(2, RoundingMode.HALF_UP);
	}

	private BigDecimal orZero(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	private BigDecimal sum(List<FoodLogEntry> entries, java.util.function.Function<FoodLogEntry, BigDecimal> extractor) {
		return entries.stream().map(extractor).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
