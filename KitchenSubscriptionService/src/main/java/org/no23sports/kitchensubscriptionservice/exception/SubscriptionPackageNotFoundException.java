package org.no23sports.kitchensubscriptionservice.exception;

public class SubscriptionPackageNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public SubscriptionPackageNotFoundException(int id) {
        super("Subscription package not found with id: " + id);
    }
}
