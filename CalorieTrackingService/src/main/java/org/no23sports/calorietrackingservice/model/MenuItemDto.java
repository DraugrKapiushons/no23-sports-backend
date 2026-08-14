package org.no23sports.calorietrackingservice.model;

import java.math.BigDecimal;

// Read-only shape of a menu-service KitchenMenuItem, as seen over the wire
// through MenuServiceClient. This service never persists or owns menu items
// - it only reads the macro fields it needs when a food log entry is logged
// "from the menu" instead of as a free-text custom entry.
public class MenuItemDto {
	private int id;
	private String name;
	private MenuCategory category;
	private BigDecimal calories;
	private BigDecimal protein;
	private BigDecimal carbs;
	private BigDecimal fat;

	public MenuItemDto() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuCategory getCategory() {
		return category;
	}

	public void setCategory(MenuCategory category) {
		this.category = category;
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
