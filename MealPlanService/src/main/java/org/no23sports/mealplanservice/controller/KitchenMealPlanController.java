package org.no23sports.mealplanservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.mealplanservice.model.CreateMealPlanRequest;
import org.no23sports.mealplanservice.model.KitchenMealPlan;
import org.no23sports.mealplanservice.model.KitchenMealPlanDay;
import org.no23sports.mealplanservice.model.KitchenMealPlanItem;
import org.no23sports.mealplanservice.service.KitchenMealPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchen/meal-plans")
public class KitchenMealPlanController {

	@Autowired
	private KitchenMealPlanService service;

	@PostMapping
	public ResponseEntity<KitchenMealPlan> createMealPlan(@RequestBody CreateMealPlanRequest request) {
		KitchenMealPlan plan = service.createMealPlan(request.getUserId(), request.getGoal(),
				request.getDailyCalorieTarget(), request.getDailyProteinTarget(), request.getDailyCarbTarget(),
				request.getDailyFatTarget(), request.getStartDate(), request.getNumberOfDays());
		return ResponseEntity.ok(plan);
	}

	@GetMapping("/{id}")
	public ResponseEntity<KitchenMealPlan> getMealPlan(@PathVariable int id) {
		return ResponseEntity.ok(service.getMealPlan(id));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<KitchenMealPlan>> getMealPlansForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getMealPlansForUser(userId));
	}

	@GetMapping("/{id}/days")
	public ResponseEntity<List<KitchenMealPlanDay>> getDaysForPlan(@PathVariable int id) {
		return ResponseEntity.ok(service.getDaysForPlan(id));
	}

	@GetMapping("/days/{dayId}/items")
	public ResponseEntity<List<KitchenMealPlanItem>> getItemsForDay(@PathVariable int dayId) {
		return ResponseEntity.ok(service.getItemsForDay(dayId));
	}
}
