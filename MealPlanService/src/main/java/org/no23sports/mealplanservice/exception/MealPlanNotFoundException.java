package org.no23sports.mealplanservice.exception;

public class MealPlanNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public MealPlanNotFoundException(int id) {
        super("Meal plan not found with id: " + id);
    }
}
