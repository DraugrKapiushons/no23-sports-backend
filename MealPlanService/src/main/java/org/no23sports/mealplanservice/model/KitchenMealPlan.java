package org.no23sports.mealplanservice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitchen_meal_plans")
public class KitchenMealPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "user_id", nullable = false)
	private UUID userId;
	@Enumerated(EnumType.STRING)
	private Goal goal;
	private BigDecimal dailyCalorieTarget;
	private BigDecimal dailyProteinTarget;
	private BigDecimal dailyCarbTarget;
	private BigDecimal dailyFatTarget;
	private LocalDate startDate;

	public KitchenMealPlan() {}

	public KitchenMealPlan(UUID userId, Goal goal, BigDecimal dailyCalorieTarget, BigDecimal dailyProteinTarget,
			BigDecimal dailyCarbTarget, BigDecimal dailyFatTarget, LocalDate startDate) {
		this.userId = userId;
		this.goal = goal;
		this.dailyCalorieTarget = dailyCalorieTarget;
		this.dailyProteinTarget = dailyProteinTarget;
		this.dailyCarbTarget = dailyCarbTarget;
		this.dailyFatTarget = dailyFatTarget;
		this.startDate = startDate;
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public Goal getGoal() {
		return goal;
	}

	public BigDecimal getDailyCalorieTarget() {
		return dailyCalorieTarget;
	}

	public BigDecimal getDailyProteinTarget() {
		return dailyProteinTarget;
	}

	public BigDecimal getDailyCarbTarget() {
		return dailyCarbTarget;
	}

	public BigDecimal getDailyFatTarget() {
		return dailyFatTarget;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
}
