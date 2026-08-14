package org.no23sports.mealplanservice.model;

import java.math.BigDecimal;
import java.util.List;

// Read-only shape of a menu-service KitchenMenuItem, as seen over the wire
// through MenuServiceClient. This service never persists or owns menu items —
// it only reads the fields KitchenPlanMatchingService needs to build a plan.
public class MenuItemDto {
	private int id;
	private String name;
	private MenuCategory category;
	private BigDecimal price;
	private BigDecimal calories;
	private BigDecimal protein;
	private BigDecimal carbs;
	private BigDecimal fat;
	private List<DietaryTag> dietaryTags;

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public List<DietaryTag> getDietaryTags() {
		return dietaryTags;
	}

	public void setDietaryTags(List<DietaryTag> dietaryTags) {
		this.dietaryTags = dietaryTags;
	}
}
