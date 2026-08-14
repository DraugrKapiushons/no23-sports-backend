package org.no23sports.mealplanservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// menuItemId is a plain int, not a JPA relationship: the menu item it points
// to is owned by menu-service, not this service, so there is no FK/join
// across the database boundary — only an id kept as a reference plus the
// snapshot fields carried on the plan item (see PlanItemMatch).
@Entity
@Table(name = "kitchen_meal_plan_items")
public class KitchenMealPlanItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int mealPlanDayId;
	private int menuItemId;
	@Enumerated(EnumType.STRING)
	private MealType mealType;
	private int quantity;

	public KitchenMealPlanItem() {}

	public KitchenMealPlanItem(int mealPlanDayId, int menuItemId, MealType mealType, int quantity) {
		this.mealPlanDayId = mealPlanDayId;
		this.menuItemId = menuItemId;
		this.mealType = mealType;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public int getMealPlanDayId() {
		return mealPlanDayId;
	}

	public int getMenuItemId() {
		return menuItemId;
	}

	public MealType getMealType() {
		return mealType;
	}

	public int getQuantity() {
		return quantity;
	}
}
