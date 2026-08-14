package org.no23sports.calorietrackingservice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Spec section 7's "Kalori Takip Paneli": bugünkü hedef, tüketilen öğünler,
// alınan protein/karbonhidrat/yağ/kalori, su tüketimi - all in one call so
// the panel doesn't need to stitch together separate goal/meal/water
// requests itself. goalXxx fields come from UserProfileService's nutrition
// calculator; consumedXxx/remainingXxx are computed from this service's own
// log entries for the date.
public class DailySummaryResponse {
	private LocalDate date;

	private BigDecimal goalCalories;
	private BigDecimal goalProtein;
	private BigDecimal goalCarbs;
	private BigDecimal goalFat;

	private BigDecimal consumedCalories;
	private BigDecimal consumedProtein;
	private BigDecimal consumedCarbs;
	private BigDecimal consumedFat;

	private BigDecimal remainingCalories;
	private BigDecimal remainingProtein;
	private BigDecimal remainingCarbs;
	private BigDecimal remainingFat;

	private int waterConsumedMl;
	private int waterGoalMl;

	private List<FoodLogEntry> meals;
	private List<WaterLogEntry> waterEntries;

	public DailySummaryResponse() {}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getGoalCalories() {
		return goalCalories;
	}

	public void setGoalCalories(BigDecimal goalCalories) {
		this.goalCalories = goalCalories;
	}

	public BigDecimal getGoalProtein() {
		return goalProtein;
	}

	public void setGoalProtein(BigDecimal goalProtein) {
		this.goalProtein = goalProtein;
	}

	public BigDecimal getGoalCarbs() {
		return goalCarbs;
	}

	public void setGoalCarbs(BigDecimal goalCarbs) {
		this.goalCarbs = goalCarbs;
	}

	public BigDecimal getGoalFat() {
		return goalFat;
	}

	public void setGoalFat(BigDecimal goalFat) {
		this.goalFat = goalFat;
	}

	public BigDecimal getConsumedCalories() {
		return consumedCalories;
	}

	public void setConsumedCalories(BigDecimal consumedCalories) {
		this.consumedCalories = consumedCalories;
	}

	public BigDecimal getConsumedProtein() {
		return consumedProtein;
	}

	public void setConsumedProtein(BigDecimal consumedProtein) {
		this.consumedProtein = consumedProtein;
	}

	public BigDecimal getConsumedCarbs() {
		return consumedCarbs;
	}

	public void setConsumedCarbs(BigDecimal consumedCarbs) {
		this.consumedCarbs = consumedCarbs;
	}

	public BigDecimal getConsumedFat() {
		return consumedFat;
	}

	public void setConsumedFat(BigDecimal consumedFat) {
		this.consumedFat = consumedFat;
	}

	public BigDecimal getRemainingCalories() {
		return remainingCalories;
	}

	public void setRemainingCalories(BigDecimal remainingCalories) {
		this.remainingCalories = remainingCalories;
	}

	public BigDecimal getRemainingProtein() {
		return remainingProtein;
	}

	public void setRemainingProtein(BigDecimal remainingProtein) {
		this.remainingProtein = remainingProtein;
	}

	public BigDecimal getRemainingCarbs() {
		return remainingCarbs;
	}

	public void setRemainingCarbs(BigDecimal remainingCarbs) {
		this.remainingCarbs = remainingCarbs;
	}

	public BigDecimal getRemainingFat() {
		return remainingFat;
	}

	public void setRemainingFat(BigDecimal remainingFat) {
		this.remainingFat = remainingFat;
	}

	public int getWaterConsumedMl() {
		return waterConsumedMl;
	}

	public void setWaterConsumedMl(int waterConsumedMl) {
		this.waterConsumedMl = waterConsumedMl;
	}

	public int getWaterGoalMl() {
		return waterGoalMl;
	}

	public void setWaterGoalMl(int waterGoalMl) {
		this.waterGoalMl = waterGoalMl;
	}

	public List<FoodLogEntry> getMeals() {
		return meals;
	}

	public void setMeals(List<FoodLogEntry> meals) {
		this.meals = meals;
	}

	public List<WaterLogEntry> getWaterEntries() {
		return waterEntries;
	}

	public void setWaterEntries(List<WaterLogEntry> waterEntries) {
		this.waterEntries = waterEntries;
	}
}
