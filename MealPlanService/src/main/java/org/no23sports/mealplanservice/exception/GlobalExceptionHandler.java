package org.no23sports.mealplanservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MealPlanNotFoundException.class)
    public ResponseEntity<String> handleMealPlanNotFound(MealPlanNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(KitchenPlanGenerationException.class)
    public ResponseEntity<String> handlePlanGenerationFailure(KitchenPlanGenerationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ex.getMessage());
    }

    @ExceptionHandler(MenuServiceUnavailableException.class)
    public ResponseEntity<String> handleMenuServiceUnavailable(MenuServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }
}
