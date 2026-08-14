package org.no23sports.calorietrackingservice.model;

import java.math.BigDecimal;

// Read-only shape of UserProfileService's calorie-calculator Response
// (GET /profile/nutrition/user/{userId}) - the same daily kcal/protein/
// carb/fat targets shown on the "Kalori Hesaplama" page, reused here as the
// goal line for the tracking panel instead of duplicating that math.
public class NutritionGoalDto {
	private BigDecimal dailyCalories;
	private BigDecimal protein;
	private BigDecimal carbohidrates;
	private BigDecimal fats;

	public NutritionGoalDto() {}

	public BigDecimal getDailyCalories() {
		return dailyCalories;
	}

	public void setDailyCalories(BigDecimal dailyCalories) {
		this.dailyCalories = dailyCalories;
	}

	public BigDecimal getProtein() {
		return protein;
	}

	public void setProtein(BigDecimal protein) {
		this.protein = protein;
	}

	public BigDecimal getCarbohidrates() {
		return carbohidrates;
	}

	public void setCarbohidrates(BigDecimal carbohidrates) {
		this.carbohidrates = carbohidrates;
	}

	public BigDecimal getFats() {
		return fats;
	}

	public void setFats(BigDecimal fats) {
		this.fats = fats;
	}
}
