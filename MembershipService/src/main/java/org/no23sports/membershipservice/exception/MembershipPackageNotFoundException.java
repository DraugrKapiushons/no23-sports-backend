package org.no23sports.membershipservice.exception;

public class MembershipPackageNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public MembershipPackageNotFoundException(int id) {
        super("Membership package not found with id: " + id);
    }
}
