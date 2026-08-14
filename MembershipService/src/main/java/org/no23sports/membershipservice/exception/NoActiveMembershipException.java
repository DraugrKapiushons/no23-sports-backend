package org.no23sports.membershipservice.exception;

import java.util.UUID;

public class NoActiveMembershipException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public NoActiveMembershipException(UUID userId) {
        super("No active membership found for user: " + userId);
    }
}
