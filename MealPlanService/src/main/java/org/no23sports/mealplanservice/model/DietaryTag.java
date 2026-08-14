package org.no23sports.mealplanservice.model;

// Mirrors menu-service's DietaryTag. Duplicated intentionally: this service
// only needs it to read the tags menu-service already put in a MenuItemDto,
// not to own the vocabulary. Keep the two enums in sync if menu-service adds
// values (see MenuServiceClient for the DTO the values are read into).
public enum DietaryTag {
	HIGH_PROTEIN,
	LOW_CALORIE,
	GLUTEN_FREE,
	VEGETARIAN
}
