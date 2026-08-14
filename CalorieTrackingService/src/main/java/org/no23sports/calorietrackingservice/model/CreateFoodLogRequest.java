package org.no23sports.calorietrackingservice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// Either menuItemId is set (macros are fetched from menu-service and scaled
// by quantity) or name/calories/protein/carbs/fat are supplied directly for
// a free-text custom entry - see CalorieTrackingService.logFood for the
// branch. logDate defaults to today when omitted.
public class CreateFoodLogRequest {
	private UUID userId;
	private LocalDate logDate;
	private MealType mealType;
	private Integer menuItemId;
	private String name;
	private BigDecimal quantity;
	private BigDecimal calories;
	private BigDecimal protein;
	private BigDecimal carbs;
	private BigDecimal fat;

	public CreateFoodLogRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDate getLogDate() {
		return logDate;
	}

	public void setLogDate(LocalDate logDate) {
		this.logDate = logDate;
	}

	public MealType getMealType() {
		return mealType;
	}

	public void setMealType(MealType mealType) {
		this.mealType = mealType;
	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCalories() {
		return calories;
	}

	public void setCalories(BigDecimal calories) {
		this.calories = calories;
	}

	public BigDecimal getProtein() {
		return protein;
	}

	public void setProtein(BigDecimal protein) {
		this.protein = protein;
	}

	public BigDecimal getCarbs() {
		return carbs;
	}

	public void setCarbs(BigDecimal carbs) {
		this.carbs = carbs;
	}

	public BigDecimal getFat() {
		return fat;
	}

	public void setFat(BigDecimal fat) {
		this.fat = fat;
	}
}
