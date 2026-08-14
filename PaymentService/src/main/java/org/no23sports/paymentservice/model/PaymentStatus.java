package org.no23sports.paymentservice.model;

public enum PaymentStatus {
	// Checkout form created, waiting for the member to complete it on Iyzico's page.
	PENDING,
	SUCCESSFUL,
	FAILED,
	REFUNDED
}
