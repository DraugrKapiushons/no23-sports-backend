package org.no23sports.userprofileservice.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public UserNotFoundException(int id) {
        super("User not found with id: " + id);
    }

	public UserNotFoundException(UUID userId) {
        super("User profile not found for userId: " + userId);
    }
}
