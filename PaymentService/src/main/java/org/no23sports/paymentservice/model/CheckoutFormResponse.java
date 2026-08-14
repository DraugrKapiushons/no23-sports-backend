package org.no23sports.paymentservice.model;

public class CheckoutFormResponse {
	private int paymentId;
	private String token;
	// Hosted Iyzico payment page - simplest integration, just redirect the
	// member's browser here.
	private String paymentPageUrl;
	// Raw <script>/form HTML - use instead of paymentPageUrl if the
	// frontend wants to embed the form inline rather than redirecting.
	private String checkoutFormContent;

	public CheckoutFormResponse(int paymentId, String token, String paymentPageUrl, String checkoutFormContent) {
		this.paymentId = paymentId;
		this.token = token;
		this.paymentPageUrl = paymentPageUrl;
		this.checkoutFormContent = checkoutFormContent;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public String getToken() {
		return token;
	}

	public String getPaymentPageUrl() {
		return paymentPageUrl;
	}

	public String getCheckoutFormContent() {
		return checkoutFormContent;
	}
}
