package org.no23sports.calorietrackingservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

// One consumed item logged against spec section 7's "Kalori Takip Paneli" -
// "Tükettiği öğünler". Either logged straight from the NO23 Kitchen menu
// (menuItemId set, macros copied in at logging time so history stays
// accurate even if the menu item is edited later) or as a free-text custom
// entry (menuItemId left null, macros supplied by the member directly).
@Entity
@Table(name = "food_log_entries", indexes = {
		@Index(name = "idx_food_log_user_date", columnList = "user_id,log_date") })
public class FoodLogEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "log_date", nullable = false)
	private LocalDate logDate;

	@Enumerated(EnumType.STRING)
	private MealType mealType;

	// KitchenMenuService's KitchenMenuItem.id, when logged from the menu
	// catalog rather than as a free-text custom entry. Services stay
	// loosely coupled by id only, same convention as elsewhere in this
	// codebase (see ReservationService's lessonId/instructorId).
	private Integer menuItemId;

	@Column(nullable = false)
	private String name;

	// How many servings of the (menu or custom) item this entry represents.
	// Macros below are already scaled by this quantity at write time, so
	// summing entries for a day is a plain addition.
	@Column(nullable = false)
	private BigDecimal quantity;

	@Column(nullable = false)
	private BigDecimal calories;
	@Column(nullable = false)
	private BigDecimal protein;
	@Column(nullable = false)
	private BigDecimal carbs;
	@Column(nullable = false)
	private BigDecimal fat;

	private Instant loggedAt;

	public FoodLogEntry() {}

	public FoodLogEntry(UUID userId, LocalDate logDate, MealType mealType, Integer menuItemId, String name,
			BigDecimal quantity, BigDecimal calories, BigDecimal protein, BigDecimal carbs, BigDecimal fat) {
		this.userId = userId;
		this.logDate = logDate;
		this.mealType = mealType;
		this.menuItemId = menuItemId;
		this.name = name;
		this.quantity = quantity;
		this.calories = calories;
		this.protein = protein;
		this.carbs = carbs;
		this.fat = fat;
		this.loggedAt = Instant.now();
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public LocalDate getLogDate() {
		return logDate;
	}

	public MealType getMealType() {
		return mealType;
	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public BigDecimal getCalories() {
		return calories;
	}

	public BigDecimal getProtein() {
		return protein;
	}

	public BigDecimal getCarbs() {
		return carbs;
	}

	public BigDecimal getFat() {
		return fat;
	}

	public Instant getLoggedAt() {
		return loggedAt;
	}
}
