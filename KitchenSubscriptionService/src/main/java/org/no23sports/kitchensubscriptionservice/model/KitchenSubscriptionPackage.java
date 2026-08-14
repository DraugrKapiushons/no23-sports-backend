package org.no23sports.kitchensubscriptionservice.model;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kitchen_subscription_packages")
public class KitchenSubscriptionPackage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private SubscriptionPeriod period;
	private String description;
	private int mealsPerDay;
	private BigDecimal price;

	public KitchenSubscriptionPackage() {}

	public KitchenSubscriptionPackage(SubscriptionPeriod period, String description, int mealsPerDay,
			BigDecimal price) {
		this.period = period;
		this.description = description;
		this.mealsPerDay = mealsPerDay;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public SubscriptionPeriod getPeriod() {
		return period;
	}

	public void setPeriod(SubscriptionPeriod period) {
		this.period = period;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMealsPerDay() {
		return mealsPerDay;
	}

	public void setMealsPerDay(int mealsPerDay) {
		this.mealsPerDay = mealsPerDay;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
