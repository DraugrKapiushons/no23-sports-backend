package org.no23sports.calorietrackingservice.exception;

// Thrown when a food log entry references a menuItemId that menu-service
// doesn't recognize (404) - distinct from MenuServiceUnavailableException,
// which covers menu-service being unreachable or erroring.
public class MenuItemNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MenuItemNotFoundException(int menuItemId) {
		super("Menu item not found with id: " + menuItemId);
	}
}
