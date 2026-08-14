package org.no23sports.calorietrackingservice.exception;

public class WaterLogEntryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public WaterLogEntryNotFoundException(int id) {
		super("Water log entry not found with id: " + id);
	}
}
