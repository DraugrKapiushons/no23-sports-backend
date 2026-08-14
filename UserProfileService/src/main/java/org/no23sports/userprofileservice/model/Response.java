package org.no23sports.userprofileservice.model;

import java.math.BigDecimal;

public class Response {
	private BigDecimal dailyCalories;
	private BigDecimal protein;
	private BigDecimal carbohidrates;
	private BigDecimal fats;
	
	public Response(BigDecimal dailyCalories, BigDecimal protein, BigDecimal carbohidrates, BigDecimal fats) {
		this.dailyCalories = dailyCalories;
		this.protein = protein;
		this.carbohidrates = carbohidrates;
		this.fats = fats;
	}
	
	public BigDecimal getDailyCalories() {
		return dailyCalories;
	}
	
	public BigDecimal getProtein() {
		return protein;
	}
	
	public BigDecimal getCarbohidrates() {
		return carbohidrates;
	}
	
	public BigDecimal getFats() {
		return fats;
	}
}
