package org.no23sports.kitchensubscriptionservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.kitchensubscriptionservice.model.KitchenSubscription;
import org.no23sports.kitchensubscriptionservice.model.KitchenSubscriptionPackage;
import org.no23sports.kitchensubscriptionservice.model.SubscribeRequest;
import org.no23sports.kitchensubscriptionservice.service.KitchenSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchen/subscriptions")
public class KitchenSubscriptionController {

	@Autowired
	private KitchenSubscriptionService service;

	@GetMapping("/packages")
	public ResponseEntity<List<KitchenSubscriptionPackage>> getPackages() {
		return ResponseEntity.ok(service.getAllPackages());
	}

	@PostMapping
	public ResponseEntity<KitchenSubscription> subscribe(@RequestBody SubscribeRequest request) {
		return ResponseEntity.ok(service.subscribe(request.getUserId(), request.getPackageId()));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<KitchenSubscription>> getSubscriptionsForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getSubscriptionsForUser(userId));
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<KitchenSubscription> cancelSubscription(@PathVariable int id) {
		return ResponseEntity.ok(service.cancelSubscription(id));
	}
}
