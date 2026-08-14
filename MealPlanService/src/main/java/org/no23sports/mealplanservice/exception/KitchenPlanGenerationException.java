package org.no23sports.mealplanservice.exception;

public class KitchenPlanGenerationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public KitchenPlanGenerationException(String message) {
        super(message);
    }
}
