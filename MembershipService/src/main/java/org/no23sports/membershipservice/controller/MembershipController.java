package org.no23sports.membershipservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.membershipservice.model.Membership;
import org.no23sports.membershipservice.model.MembershipPackage;
import org.no23sports.membershipservice.model.SubscribeRequest;
import org.no23sports.membershipservice.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/membership")
public class MembershipController {

	@Autowired
	private MembershipService service;

	@GetMapping("/packages")
	public ResponseEntity<List<MembershipPackage>> getPackages() {
		return ResponseEntity.ok(service.getAllPackages());
	}

	@GetMapping("/packages/{id}")
	public ResponseEntity<MembershipPackage> getPackage(@PathVariable int id) {
		return ResponseEntity.ok(service.getPackage(id));
	}

	@PostMapping("/packages")
	public ResponseEntity<?> addPackage(@RequestBody MembershipPackage pkg, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.addPackage(pkg));
	}

	@PutMapping("/packages/{id}")
	public ResponseEntity<?> updatePackage(@PathVariable int id, @RequestBody MembershipPackage pkg,
			HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.updatePackage(id, pkg));
	}

	@DeleteMapping("/packages/{id}")
	public ResponseEntity<?> deletePackage(@PathVariable int id, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		service.deletePackage(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping
	public ResponseEntity<Membership> subscribe(@RequestBody SubscribeRequest request) {
		return ResponseEntity.ok(service.subscribe(request.getUserId(), request.getPackageId(), request.isAutoRenew()));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Membership>> getMembershipsForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getMembershipsForUser(userId));
	}

	@GetMapping("/user/{userId}/active")
	public ResponseEntity<Membership> getActiveMembership(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getActiveMembership(userId));
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<Membership> cancelMembership(@PathVariable int id) {
		return ResponseEntity.ok(service.cancelMembership(id));
	}

	@PostMapping("/{id}/pause")
	public ResponseEntity<Membership> pauseMembership(@PathVariable int id) {
		return ResponseEntity.ok(service.pauseMembership(id));
	}

	@PostMapping("/{id}/resume")
	public ResponseEntity<Membership> resumeMembership(@PathVariable int id) {
		return ResponseEntity.ok(service.resumeMembership(id));
	}

	// Package CRUD is an admin-panel action (spec "Paket oluşturma").
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
