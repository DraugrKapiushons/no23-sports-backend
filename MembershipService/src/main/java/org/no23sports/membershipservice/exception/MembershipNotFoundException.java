package org.no23sports.membershipservice.exception;

public class MembershipNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public MembershipNotFoundException(int id) {
        super("Membership not found with id: " + id);
    }
}
