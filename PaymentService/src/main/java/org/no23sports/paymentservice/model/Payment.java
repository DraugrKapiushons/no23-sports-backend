package org.no23sports.paymentservice.model;

import java.math.BigDecimal;
import java.time.Instant;
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
@Table(name = "payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Enumerated(EnumType.STRING)
	private PaymentReferenceType referenceType;

	// Id of the record being paid for in its own service (subscription id,
	// order id, membership package id, ...). Kept as a plain string since it
	// may be a UUID or an int depending on the owning service.
	private String referenceId;

	// One conversationId per checkout attempt - sent to Iyzico and echoed
	// back on the callback, used to look the Payment row back up.
	@Column(unique = true, nullable = false)
	private String conversationId;

	private String basketId;
	private String iyzicoToken;
	private String iyzicoPaymentId;

	private BigDecimal price;
	private BigDecimal paidPrice;
	private String currency;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	private String failureReason;

	private Instant createdAt;
	private Instant updatedAt;

	public Payment() {}

	public Payment(UUID userId, PaymentReferenceType referenceType, String referenceId, String conversationId,
			String basketId, BigDecimal price, String currency) {
		this.userId = userId;
		this.referenceType = referenceType;
		this.referenceId = referenceId;
		this.conversationId = conversationId;
		this.basketId = basketId;
		this.price = price;
		this.currency = currency;
		this.status = PaymentStatus.PENDING;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public PaymentReferenceType getReferenceType() {
		return referenceType;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public String getBasketId() {
		return basketId;
	}

	public String getIyzicoToken() {
		return iyzicoToken;
	}

	public void setIyzicoToken(String iyzicoToken) {
		this.iyzicoToken = iyzicoToken;
	}

	public String getIyzicoPaymentId() {
		return iyzicoPaymentId;
	}

	public void setIyzicoPaymentId(String iyzicoPaymentId) {
		this.iyzicoPaymentId = iyzicoPaymentId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(BigDecimal paidPrice) {
		this.paidPrice = paidPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
		this.updatedAt = Instant.now();
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
