package org.no23sports.kitchenmenuservice.controller;

import java.util.List;

import org.no23sports.kitchenmenuservice.model.DietaryTag;
import org.no23sports.kitchenmenuservice.model.KitchenIngredient;
import org.no23sports.kitchenmenuservice.model.KitchenMenuItem;
import org.no23sports.kitchenmenuservice.model.MenuCategory;
import org.no23sports.kitchenmenuservice.service.KitchenMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/kitchen/menu")
public class KitchenMenuController {

	@Autowired
	private KitchenMenuService service;

	@GetMapping
	public ResponseEntity<List<KitchenMenuItem>> getMenuItems(
			@RequestParam(required = false) MenuCategory category,
			@RequestParam(required = false) DietaryTag dietaryTag) {
		if (category != null) {
			return ResponseEntity.ok(service.getMenuItemsByCategory(category));
		}
		if (dietaryTag != null) {
			return ResponseEntity.ok(service.getMenuItemsByDietaryTag(dietaryTag));
		}
		return ResponseEntity.ok(service.getAllMenuItems());
	}

	@GetMapping("/{id}")
	public ResponseEntity<KitchenMenuItem> getMenuItem(@PathVariable int id) {
		return ResponseEntity.ok(service.getMenuItem(id));
	}

	@PostMapping
	public ResponseEntity<?> createMenuItem(@RequestBody KitchenMenuItem menuItem, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.createMenuItem(menuItem));
	}

	@GetMapping("/ingredients/{id}")
	public ResponseEntity<KitchenIngredient> getIngredient(@PathVariable int id) {
		return ResponseEntity.ok(service.getIngredient(id));
	}

	@PostMapping("/ingredients")
	public ResponseEntity<?> createIngredient(@RequestBody KitchenIngredient ingredient, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.createIngredient(ingredient));
	}

	// Menu management is an admin-panel action (spec "NO23 Kitchen - Menü yönetimi").
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
