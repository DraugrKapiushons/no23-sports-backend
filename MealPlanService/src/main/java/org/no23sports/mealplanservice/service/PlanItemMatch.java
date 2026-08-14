package org.no23sports.mealplanservice.service;

import java.math.BigDecimal;

import org.no23sports.mealplanservice.model.MealType;

// Per-unit snapshot values (not multiplied by quantity) taken from
// menu-service's response at generation time, so a plan keeps a record of
// what a menu item looked like even if it's later edited or removed in
// menu-service's own database.
public record PlanItemMatch(
		int menuItemId,
		MealType slot,
		int quantity,
		String productNameSnapshot,
		BigDecimal caloriesSnapshot,
		BigDecimal proteinSnapshot,
		BigDecimal carbsSnapshot,
		BigDecimal fatSnapshot,
		BigDecimal unitPriceSnapshot) {
}
