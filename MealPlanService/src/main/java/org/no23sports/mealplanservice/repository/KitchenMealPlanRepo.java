package org.no23sports.mealplanservice.repository;

import java.util.List;
import java.util.UUID;

import org.no23sports.mealplanservice.model.KitchenMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenMealPlanRepo extends JpaRepository<KitchenMealPlan, Integer> {
	List<KitchenMealPlan> findByUserId(UUID userId);
}
