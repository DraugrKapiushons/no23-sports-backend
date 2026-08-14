package org.no23sports.paymentservice.model;

import java.math.BigDecimal;

public class RefundRequest {
	// Null/omitted = refund the full paidPrice.
	private BigDecimal amount;
	private String reason;

	public RefundRequest() {}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
