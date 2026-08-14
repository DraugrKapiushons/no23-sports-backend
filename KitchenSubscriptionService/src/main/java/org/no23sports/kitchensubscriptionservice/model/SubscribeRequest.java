package org.no23sports.kitchensubscriptionservice.model;

import java.util.UUID;

public class SubscribeRequest {
	private UUID userId;
	private int packageId;

	public SubscribeRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
}
