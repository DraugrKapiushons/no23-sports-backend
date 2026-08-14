package org.no23sports.kitchensubscriptionservice.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public SubscriptionNotFoundException(int id) {
        super("Subscription not found with id: " + id);
    }
}
