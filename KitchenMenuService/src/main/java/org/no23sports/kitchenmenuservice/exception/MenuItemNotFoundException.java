package org.no23sports.kitchenmenuservice.exception;

public class MenuItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public MenuItemNotFoundException(int id) {
        super("Menu item not found with id: " + id);
    }
}
