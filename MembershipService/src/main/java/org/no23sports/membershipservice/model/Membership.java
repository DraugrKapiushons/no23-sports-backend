package org.no23sports.membershipservice.model;

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
@Table(name = "memberships")
public class Membership {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "user_id", nullable = false)
	private UUID userId;
	private int packageId;
	private LocalDate startDate;
	private LocalDate endDate;
	@Enumerated(EnumType.STRING)
	private MembershipStatus status;
	private boolean autoRenew;

	public Membership() {}

	public Membership(UUID userId, int packageId, LocalDate startDate, LocalDate endDate, MembershipStatus status,
			boolean autoRenew) {
		this.userId = userId;
		this.packageId = packageId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.autoRenew = autoRenew;
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

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public MembershipStatus getStatus() {
		return status;
	}

	public void setStatus(MembershipStatus status) {
		this.status = status;
	}

	public boolean isAutoRenew() {
		return autoRenew;
	}

	public void setAutoRenew(boolean autoRenew) {
		this.autoRenew = autoRenew;
	}
}
