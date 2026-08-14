package org.no23sports.kitchenmenuservice.repository;

import java.util.List;

import org.no23sports.kitchenmenuservice.model.KitchenRecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenRecipeIngredientRepo extends JpaRepository<KitchenRecipeIngredient, Integer> {
	List<KitchenRecipeIngredient> findByMenuItemId(int menuItemId);
}
