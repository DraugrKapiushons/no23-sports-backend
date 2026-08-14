package org.no23sports.mealplanservice.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitchen_meal_plan_days")
public class KitchenMealPlanDay {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int mealPlanId;
	private int dayNumber;
	private LocalDate date;

	public KitchenMealPlanDay() {}

	public KitchenMealPlanDay(int mealPlanId, int dayNumber, LocalDate date) {
		this.mealPlanId = mealPlanId;
		this.dayNumber = dayNumber;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public int getMealPlanId() {
		return mealPlanId;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public LocalDate getDate() {
		return date;
	}
}
