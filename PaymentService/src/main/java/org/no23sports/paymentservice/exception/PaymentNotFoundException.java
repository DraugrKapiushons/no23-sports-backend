package org.no23sports.paymentservice.exception;

public class PaymentNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PaymentNotFoundException(int id) {
		super("Payment not found with id: " + id);
	}

	public PaymentNotFoundException(String conversationId) {
		super("Payment not found with conversationId: " + conversationId);
	}
}
