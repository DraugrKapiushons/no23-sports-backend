package org.no23sports.calorietrackingservice.exception;

public class UserProfileServiceUnavailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserProfileServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}
}
