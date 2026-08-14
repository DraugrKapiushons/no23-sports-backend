package org.no23sports.calorietrackingservice.model;

import java.time.LocalDate;
import java.util.UUID;

public class CreateWaterLogRequest {
	private UUID userId;
	private LocalDate logDate;
	private int amountMl;

	public CreateWaterLogRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public LocalDate getLogDate() {
		return logDate;
	}

	public void setLogDate(LocalDate logDate) {
		this.logDate = logDate;
	}

	public int getAmountMl() {
		return amountMl;
	}

	public void setAmountMl(int amountMl) {
		this.amountMl = amountMl;
	}
}
