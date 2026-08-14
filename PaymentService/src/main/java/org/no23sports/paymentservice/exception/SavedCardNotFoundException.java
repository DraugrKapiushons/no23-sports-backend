package org.no23sports.paymentservice.exception;

public class SavedCardNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SavedCardNotFoundException(int id) {
		super("Saved card not found with id: " + id);
	}
}
