package org.no23sports.mealplanservice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateMealPlanRequest {
	private UUID userId;
	private Goal goal;
	private BigDecimal dailyCalorieTarget;
	private BigDecimal dailyProteinTarget;
	private BigDecimal dailyCarbTarget;
	private BigDecimal dailyFatTarget;
	private LocalDate startDate;
	private int numberOfDays;

	public CreateMealPlanRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public BigDecimal getDailyCalorieTarget() {
		return dailyCalorieTarget;
	}

	public void setDailyCalorieTarget(BigDecimal dailyCalorieTarget) {
		this.dailyCalorieTarget = dailyCalorieTarget;
	}

	public BigDecimal getDailyProteinTarget() {
		return dailyProteinTarget;
	}

	public void setDailyProteinTarget(BigDecimal dailyProteinTarget) {
		this.dailyProteinTarget = dailyProteinTarget;
	}

	public BigDecimal getDailyCarbTarget() {
		return dailyCarbTarget;
	}

	public void setDailyCarbTarget(BigDecimal dailyCarbTarget) {
		this.dailyCarbTarget = dailyCarbTarget;
	}

	public BigDecimal getDailyFatTarget() {
		return dailyFatTarget;
	}

	public void setDailyFatTarget(BigDecimal dailyFatTarget) {
		this.dailyFatTarget = dailyFatTarget;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
}
