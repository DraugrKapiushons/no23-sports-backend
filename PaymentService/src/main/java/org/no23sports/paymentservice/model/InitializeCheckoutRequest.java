package org.no23sports.paymentservice.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class InitializeCheckoutRequest {
	private UUID userId;
	private PaymentReferenceType referenceType;
	private String referenceId;

	private BigDecimal price;
	// Optional - defaults to price when omitted (no extra installment fee).
	private BigDecimal paidPrice;

	private String buyerName;
	private String buyerSurname;
	private String buyerEmail;
	private String buyerPhone;
	private String buyerAddress;
	private String buyerCity;
	private String buyerCountry;
	private String buyerIdentityNumber;
	private String buyerIp;

	private List<BasketItemRequest> basketItems;

	public InitializeCheckoutRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public PaymentReferenceType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(PaymentReferenceType referenceType) {
		this.referenceType = referenceType;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(BigDecimal paidPrice) {
		this.paidPrice = paidPrice;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerSurname() {
		return buyerSurname;
	}

	public void setBuyerSurname(String buyerSurname) {
		this.buyerSurname = buyerSurname;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getBuyerCity() {
		return buyerCity;
	}

	public void setBuyerCity(String buyerCity) {
		this.buyerCity = buyerCity;
	}

	public String getBuyerCountry() {
		return buyerCountry;
	}

	public void setBuyerCountry(String buyerCountry) {
		this.buyerCountry = buyerCountry;
	}

	public String getBuyerIdentityNumber() {
		return buyerIdentityNumber;
	}

	public void setBuyerIdentityNumber(String buyerIdentityNumber) {
		this.buyerIdentityNumber = buyerIdentityNumber;
	}

	public String getBuyerIp() {
		return buyerIp;
	}

	public void setBuyerIp(String buyerIp) {
		this.buyerIp = buyerIp;
	}

	public List<BasketItemRequest> getBasketItems() {
		return basketItems;
	}

	public void setBasketItems(List<BasketItemRequest> basketItems) {
		this.basketItems = basketItems;
	}
}
