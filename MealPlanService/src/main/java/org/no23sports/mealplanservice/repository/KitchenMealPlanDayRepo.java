package org.no23sports.mealplanservice.repository;

import java.util.List;

import org.no23sports.mealplanservice.model.KitchenMealPlanDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenMealPlanDayRepo extends JpaRepository<KitchenMealPlanDay, Integer> {
	List<KitchenMealPlanDay> findByMealPlanId(int mealPlanId);
}
