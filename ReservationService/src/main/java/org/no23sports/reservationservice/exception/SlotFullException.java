package org.no23sports.reservationservice.exception;

public class SlotFullException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SlotFullException(Integer lessonId, int capacity) {
		super("Lesson " + lessonId + " is already fully booked for this time slot (capacity: " + capacity + ")");
	}
}
