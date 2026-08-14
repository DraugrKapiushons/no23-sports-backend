package org.no23sports.kitchenmenuservice.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitchen_ingredients")
public class KitchenIngredient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private BigDecimal caloriesPer100g;
	private BigDecimal proteinPer100g;
	private BigDecimal carbsPer100g;
	private BigDecimal fatPer100g;

	public KitchenIngredient() {}

	public KitchenIngredient(String name, BigDecimal caloriesPer100g, BigDecimal proteinPer100g,
			BigDecimal carbsPer100g, BigDecimal fatPer100g) {
		this.name = name;
		this.caloriesPer100g = caloriesPer100g;
		this.proteinPer100g = proteinPer100g;
		this.carbsPer100g = carbsPer100g;
		this.fatPer100g = fatPer100g;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCaloriesPer100g() {
		return caloriesPer100g;
	}

	public void setCaloriesPer100g(BigDecimal caloriesPer100g) {
		this.caloriesPer100g = caloriesPer100g;
	}

	public BigDecimal getProteinPer100g() {
		return proteinPer100g;
	}

	public void setProteinPer100g(BigDecimal proteinPer100g) {
		this.proteinPer100g = proteinPer100g;
	}

	public BigDecimal getCarbsPer100g() {
		return carbsPer100g;
	}

	public void setCarbsPer100g(BigDecimal carbsPer100g) {
		this.carbsPer100g = carbsPer100g;
	}

	public BigDecimal getFatPer100g() {
		return fatPer100g;
	}

	public void setFatPer100g(BigDecimal fatPer100g) {
		this.fatPer100g = fatPer100g;
	}
}
