package org.no23sports.calorietrackingservice.exception;

public class FoodLogEntryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FoodLogEntryNotFoundException(int id) {
		super("Food log entry not found with id: " + id);
	}
}
