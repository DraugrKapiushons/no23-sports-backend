package org.no23sports.mealplanservice.exception;

// Thrown when the call to menu-service (to fetch the menu items a plan is
// built from) fails or times out. Kept distinct from
// KitchenPlanGenerationException so callers/monitoring can tell "menu-service
// is down" apart from "no combination of menu items fit these targets".
public class MenuServiceUnavailableException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public MenuServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
