package org.no23sports.reservationservice.exception;

// Thrown when an action doesn't make sense for the reservation's current
// status - e.g. confirming a reservation that was already cancelled.
public class InvalidReservationStateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidReservationStateException(String message) {
		super(message);
	}
}
