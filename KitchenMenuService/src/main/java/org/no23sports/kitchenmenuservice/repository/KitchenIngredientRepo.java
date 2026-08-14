package org.no23sports.kitchenmenuservice.repository;

import org.no23sports.kitchenmenuservice.model.KitchenIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenIngredientRepo extends JpaRepository<KitchenIngredient, Integer> {
}
