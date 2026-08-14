package org.no23sports.paymentservice.model;

import java.util.UUID;

// Card data (number/expiry/cvc) touches this service just long enough to be
// forwarded straight to Iyzico over TLS for tokenization - it is never
// persisted here. Only the resulting cardUserKey/cardToken are stored.
public class SaveCardRequest {
	private UUID userId;
	private String cardAlias;
	private String cardHolderName;
	private String cardNumber;
	private String expireMonth;
	private String expireYear;

	public SaveCardRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getCardAlias() {
		return cardAlias;
	}

	public void setCardAlias(String cardAlias) {
		this.cardAlias = cardAlias;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(String expireMonth) {
		this.expireMonth = expireMonth;
	}

	public String getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(String expireYear) {
		this.expireYear = expireYear;
	}
}
