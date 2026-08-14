package org.no23sports.kitchenmenuservice.repository;

import java.util.List;

import org.no23sports.kitchenmenuservice.model.KitchenMenuItem;
import org.no23sports.kitchenmenuservice.model.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenMenuItemRepo extends JpaRepository<KitchenMenuItem, Integer> {
	List<KitchenMenuItem> findByCategory(MenuCategory category);
}
