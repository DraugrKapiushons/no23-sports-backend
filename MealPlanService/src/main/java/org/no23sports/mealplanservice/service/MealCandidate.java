package org.no23sports.mealplanservice.service;

import java.math.BigDecimal;

import org.no23sports.mealplanservice.model.MealType;
import org.no23sports.mealplanservice.model.MenuItemDto;

record MealCandidate(MenuItemDto item, MealType slot, int quantity) {

	BigDecimal totalCalories() {
		return item.getCalories().multiply(BigDecimal.valueOf(quantity));
	}

	BigDecimal totalProtein() {
		return item.getProtein().multiply(BigDecimal.valueOf(quantity));
	}

	BigDecimal totalCarbs() {
		return item.getCarbs().multiply(BigDecimal.valueOf(quantity));
	}

	BigDecimal totalFat() {
		return item.getFat().multiply(BigDecimal.valueOf(quantity));
	}
}
