package org.no23sports.kitchenmenuservice.model;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitchen_menu_items")
public class KitchenMenuItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;
	private String photoUrl;
	@Enumerated(EnumType.STRING)
	private MenuCategory category;
	private BigDecimal price;
	private BigDecimal calories;
	private BigDecimal protein;
	private BigDecimal carbs;
	private BigDecimal fat;
	@ElementCollection
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "kitchen_menu_item_dietary_tags", joinColumns = @JoinColumn(name = "menu_item_id"))
	private List<DietaryTag> dietaryTags;
	@ElementCollection
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "kitchen_menu_item_allergens", joinColumns = @JoinColumn(name = "menu_item_id"))
	private List<Allergen> allergens;

	public KitchenMenuItem() {}

	public KitchenMenuItem(String name, String description, String photoUrl, MenuCategory category,
			BigDecimal price, BigDecimal calories, BigDecimal protein, BigDecimal carbs, BigDecimal fat,
			List<DietaryTag> dietaryTags, List<Allergen> allergens) {
		this.name = name;
		this.description = description;
		this.photoUrl = photoUrl;
		this.category = category;
		this.price = price;
		this.calories = calories;
		this.protein = protein;
		this.carbs = carbs;
		this.fat = fat;
		this.dietaryTags = dietaryTags;
		this.allergens = allergens;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
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

	public List<Allergen> getAllergens() {
		return allergens;
	}

	public void setAllergens(List<Allergen> allergens) {
		this.allergens = allergens;
	}
}
