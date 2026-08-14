package org.no23sports.mealplanservice.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.mealplanservice.client.MenuServiceClient;
import org.no23sports.mealplanservice.exception.MealPlanNotFoundException;
import org.no23sports.mealplanservice.model.Goal;
import org.no23sports.mealplanservice.model.KitchenMealPlan;
import org.no23sports.mealplanservice.model.KitchenMealPlanDay;
import org.no23sports.mealplanservice.model.KitchenMealPlanItem;
import org.no23sports.mealplanservice.model.MenuItemDto;
import org.no23sports.mealplanservice.repository.KitchenMealPlanDayRepo;
import org.no23sports.mealplanservice.repository.KitchenMealPlanItemRepo;
import org.no23sports.mealplanservice.repository.KitchenMealPlanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KitchenMealPlanService {

	@Autowired
	private KitchenMealPlanRepo mealPlanRepo;

	@Autowired
	private KitchenMealPlanDayRepo mealPlanDayRepo;

	@Autowired
	private KitchenMealPlanItemRepo mealPlanItemRepo;

	// Replaces the direct KitchenMenuItemRepo.findAll() the monolith used —
	// this is the one call across the new service boundary, and the only
	// place mealplan-service's scaling depends on menu-service being reachable.
	@Autowired
	private MenuServiceClient menuServiceClient;

	@Autowired
	private KitchenPlanMatchingService planMatchingService;

	public KitchenMealPlan createMealPlan(UUID userId, Goal goal, BigDecimal dailyCalorieTarget,
			BigDecimal dailyProteinTarget, BigDecimal dailyCarbTarget, BigDecimal dailyFatTarget,
			LocalDate startDate, int numberOfDays) {

		KitchenMealPlan plan = new KitchenMealPlan(userId, goal, dailyCalorieTarget, dailyProteinTarget,
				dailyCarbTarget, dailyFatTarget, startDate);
		plan = mealPlanRepo.save(plan);

		List<MenuItemDto> menuItems = menuServiceClient.getAllMenuItems();
		List<PlanDayMatch> dayMatches = planMatchingService.generate(plan, startDate, numberOfDays, menuItems);

		for (PlanDayMatch dayMatch : dayMatches) {
			KitchenMealPlanDay day = new KitchenMealPlanDay(plan.getId(), dayMatch.dayNumber(), dayMatch.planDate());
			day = mealPlanDayRepo.save(day);

			for (PlanItemMatch itemMatch : dayMatch.items()) {
				KitchenMealPlanItem item = new KitchenMealPlanItem(day.getId(), itemMatch.menuItemId(),
						itemMatch.slot(), itemMatch.quantity());
				mealPlanItemRepo.save(item);
			}
		}

		return plan;
	}

	public KitchenMealPlan getMealPlan(int id) {
		return mealPlanRepo.findById(id).orElseThrow(() -> new MealPlanNotFoundException(id));
	}

	public List<KitchenMealPlan> getMealPlansForUser(UUID userId) {
		return mealPlanRepo.findByUserId(userId);
	}

	public List<KitchenMealPlanDay> getDaysForPlan(int mealPlanId) {
		return mealPlanDayRepo.findByMealPlanId(mealPlanId);
	}

	public List<KitchenMealPlanItem> getItemsForDay(int mealPlanDayId) {
		return mealPlanItemRepo.findByMealPlanDayId(mealPlanDayId);
	}
}
