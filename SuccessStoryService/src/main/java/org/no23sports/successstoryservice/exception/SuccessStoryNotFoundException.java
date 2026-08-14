package org.no23sports.successstoryservice.exception;

public class SuccessStoryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SuccessStoryNotFoundException(int id) {
		super("Success story not found with id: " + id);
	}
}
