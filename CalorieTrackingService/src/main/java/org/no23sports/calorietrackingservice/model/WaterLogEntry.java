package org.no23sports.calorietrackingservice.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

// Spec section 7's "Su tüketimi" widget - one glass/bottle logged at a time
// so the panel can show a running total against the daily water goal.
@Entity
@Table(name = "water_log_entries", indexes = {
		@Index(name = "idx_water_log_user_date", columnList = "user_id,log_date") })
public class WaterLogEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "log_date", nullable = false)
	private LocalDate logDate;

	@Column(name = "amount_ml", nullable = false)
	private int amountMl;

	private Instant loggedAt;

	public WaterLogEntry() {}

	public WaterLogEntry(UUID userId, LocalDate logDate, int amountMl) {
		this.userId = userId;
		this.logDate = logDate;
		this.amountMl = amountMl;
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

	public int getAmountMl() {
		return amountMl;
	}

	public Instant getLoggedAt() {
		return loggedAt;
	}
}
