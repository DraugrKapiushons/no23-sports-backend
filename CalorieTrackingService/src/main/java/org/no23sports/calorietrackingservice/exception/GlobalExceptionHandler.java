package org.no23sports.calorietrackingservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FoodLogEntryNotFoundException.class)
	public ResponseEntity<String> handleFoodLogEntryNotFound(FoodLogEntryNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(WaterLogEntryNotFoundException.class)
	public ResponseEntity<String> handleWaterLogEntryNotFound(WaterLogEntryNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(MenuItemNotFoundException.class)
	public ResponseEntity<String> handleMenuItemNotFound(MenuItemNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(MenuServiceUnavailableException.class)
	public ResponseEntity<String> handleMenuServiceUnavailable(MenuServiceUnavailableException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
	}

	@ExceptionHandler(UserProfileServiceUnavailableException.class)
	public ResponseEntity<String> handleUserProfileServiceUnavailable(UserProfileServiceUnavailableException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}
