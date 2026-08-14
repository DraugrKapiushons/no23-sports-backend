package org.no23sports.authservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.authservice.model.Role;
import org.no23sports.authservice.model.UserRegistration;
import org.no23sports.authservice.model.UserRequest;
import org.no23sports.authservice.model.UserResponse;
import org.no23sports.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/auth")
public class UserController {
	
	@Autowired
	UserService service;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserRequest request) {
		UserResponse response = service.login(request);
		if (response==null) {
			return ResponseEntity.status(401).build();
		}
		
		return ResponseEntity.ok(response.getToken());
	}
	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody UserRegistration registration){
			UserResponse response = service.register(registration);
			if (response == null) {
				return ResponseEntity.status(400).build();
			}
			return ResponseEntity.ok(response.getToken());
	}

	// ---- Admin panel: Üye yönetimi ----

	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		if (!isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok(service.getAllUsers());
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
		if (!isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		UserResponse u = service.getUser(id);
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(u);
	}

	@PutMapping("/users/{id}/role")
	public ResponseEntity<UserResponse> updateRole(@PathVariable UUID id, @RequestParam Role role) {
		if (!isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		UserResponse u = service.updateRole(id, role);
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(u);
	}

	@PutMapping("/users/{id}/enabled")
	public ResponseEntity<UserResponse> setEnabled(@PathVariable UUID id, @RequestParam boolean enabled) {
		if (!isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		UserResponse u = service.setEnabled(id, enabled);
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(u);
	}

	@PostMapping("/users/{id}/unlock")
	public ResponseEntity<UserResponse> unlock(@PathVariable UUID id) {
		if (!isAdmin()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		UserResponse u = service.unlock(id);
		if (u == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(u);
	}

	private boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			return false;
		}
		return auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
	}
}
