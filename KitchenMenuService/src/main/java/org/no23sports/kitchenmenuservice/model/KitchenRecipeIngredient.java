package org.no23sports.kitchenmenuservice.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Same-database join between two entities owned by this same service, so a
// plain int FK is fine here (this is not a cross-service reference).
@Entity
@Table(name = "kitchen_recipe_ingredients")
public class KitchenRecipeIngredient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int menuItemId;
	private int ingredientId;
	private BigDecimal quantityGrams;

	public KitchenRecipeIngredient() {}

	public KitchenRecipeIngredient(int menuItemId, int ingredientId, BigDecimal quantityGrams) {
		this.menuItemId = menuItemId;
		this.ingredientId = ingredientId;
		this.quantityGrams = quantityGrams;
	}

	public int getId() {
		return id;
	}

	public int getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(int menuItemId) {
		this.menuItemId = menuItemId;
	}

	public int getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(int ingredientId) {
		this.ingredientId = ingredientId;
	}

	public BigDecimal getQuantityGrams() {
		return quantityGrams;
	}

	public void setQuantityGrams(BigDecimal quantityGrams) {
		this.quantityGrams = quantityGrams;
	}
}
