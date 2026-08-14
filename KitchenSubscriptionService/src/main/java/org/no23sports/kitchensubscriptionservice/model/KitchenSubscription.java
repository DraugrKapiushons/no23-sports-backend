package org.no23sports.kitchensubscriptionservice.model;

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
@Table(name = "kitchen_subscriptions")
public class KitchenSubscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "user_id", nullable = false)
	private UUID userId;
	private int packageId;
	private LocalDate startDate;
	private LocalDate endDate;
	@Enumerated(EnumType.STRING)
	private SubscriptionStatus status;

	public KitchenSubscription() {}

	public KitchenSubscription(UUID userId, int packageId, LocalDate startDate, LocalDate endDate,
			SubscriptionStatus status) {
		this.userId = userId;
		this.packageId = packageId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public int getPackageId() {
		return packageId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public SubscriptionStatus getStatus() {
		return status;
	}

	public void setStatus(SubscriptionStatus status) {
		this.status = status;
	}
}
