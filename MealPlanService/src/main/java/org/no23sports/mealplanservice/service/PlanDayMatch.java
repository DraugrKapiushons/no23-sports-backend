package org.no23sports.mealplanservice.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PlanDayMatch(
		int dayNumber,
		LocalDate planDate,
		BigDecimal totalCalories,
		BigDecimal totalProtein,
		BigDecimal totalCarbs,
		BigDecimal totalFat,
		List<PlanItemMatch> items) {
}
