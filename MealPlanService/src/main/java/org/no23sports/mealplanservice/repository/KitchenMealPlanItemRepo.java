package org.no23sports.mealplanservice.repository;

import java.util.List;

import org.no23sports.mealplanservice.model.KitchenMealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenMealPlanItemRepo extends JpaRepository<KitchenMealPlanItem, Integer> {
	List<KitchenMealPlanItem> findByMealPlanDayId(int mealPlanDayId);
}
