package org.no23sports.kitchenmenuservice.exception;

public class IngredientNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public IngredientNotFoundException(int id) {
        super("Ingredient not found with id: " + id);
    }
}
