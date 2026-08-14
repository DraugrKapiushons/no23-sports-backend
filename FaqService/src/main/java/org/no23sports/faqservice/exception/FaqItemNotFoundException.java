package org.no23sports.faqservice.exception;

public class FaqItemNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FaqItemNotFoundException(int id) {
		super("FAQ item not found with id: " + id);
	}
}
