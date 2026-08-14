package org.no23sports.kitchenmenuservice.service;

import java.util.List;

import org.no23sports.kitchenmenuservice.exception.IngredientNotFoundException;
import org.no23sports.kitchenmenuservice.exception.MenuItemNotFoundException;
import org.no23sports.kitchenmenuservice.model.DietaryTag;
import org.no23sports.kitchenmenuservice.model.KitchenIngredient;
import org.no23sports.kitchenmenuservice.model.KitchenMenuItem;
import org.no23sports.kitchenmenuservice.model.MenuCategory;
import org.no23sports.kitchenmenuservice.repository.KitchenIngredientRepo;
import org.no23sports.kitchenmenuservice.repository.KitchenMenuItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KitchenMenuService {

	@Autowired
	private KitchenMenuItemRepo menuItemRepo;

	@Autowired
	private KitchenIngredientRepo ingredientRepo;

	public List<KitchenMenuItem> getAllMenuItems() {
		return menuItemRepo.findAll();
	}

	public List<KitchenMenuItem> getMenuItemsByCategory(MenuCategory category) {
		return menuItemRepo.findByCategory(category);
	}

	public List<KitchenMenuItem> getMenuItemsByDietaryTag(DietaryTag tag) {
		return menuItemRepo.findAll().stream()
				.filter(item -> item.getDietaryTags() != null && item.getDietaryTags().contains(tag))
				.toList();
	}

	public KitchenMenuItem getMenuItem(int id) {
		return menuItemRepo.findById(id).orElseThrow(() -> new MenuItemNotFoundException(id));
	}

	public KitchenMenuItem createMenuItem(KitchenMenuItem menuItem) {
		return menuItemRepo.save(menuItem);
	}

	public KitchenIngredient getIngredient(int id) {
		return ingredientRepo.findById(id).orElseThrow(() -> new IngredientNotFoundException(id));
	}

	public KitchenIngredient createIngredient(KitchenIngredient ingredient) {
		return ingredientRepo.save(ingredient);
	}
}
