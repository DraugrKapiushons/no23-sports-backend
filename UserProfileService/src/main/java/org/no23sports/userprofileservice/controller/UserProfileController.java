package org.no23sports.userprofileservice.controller;

import java.util.UUID;

import org.no23sports.userprofileservice.model.Response;
import org.no23sports.userprofileservice.model.UserProfile;
import org.no23sports.userprofileservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
	
	@Autowired
	private UserProfileService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserProfile> getUserDetails(@PathVariable int id) {
		UserProfile profile = service.getUserDetails(id);
		return ResponseEntity.ok(profile);
	}
	
	@GetMapping("/nutrition/{id}")
	public ResponseEntity<Response> getUserNutrition(@PathVariable int id){
		return ResponseEntity.ok(service.getUserNutrition(id));
	}

	// UUID-keyed variants of the two endpoints above, for services that only
	// know a member by AuthService's User.uuid (e.g. CalorieTrackingService's
	// daily goal lookup) rather than this service's own internal id.
	@GetMapping("/user/{userId}")
	public ResponseEntity<UserProfile> getUserDetailsByUserId(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getUserDetailsByUserId(userId));
	}

	@GetMapping("/nutrition/user/{userId}")
	public ResponseEntity<Response> getUserNutritionByUserId(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getUserNutritionByUserId(userId));
	}
}
