package org.no23sports.successstoryservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.successstoryservice.model.CreateSuccessStoryRequest;
import org.no23sports.successstoryservice.model.SuccessCategory;
import org.no23sports.successstoryservice.model.SuccessStory;
import org.no23sports.successstoryservice.service.SuccessStoryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/success-stories")
public class SuccessStoryController {

	@Autowired
	private SuccessStoryService service;

	// Public - homepage/success-stories page listing. Optionally filtered by
	// the same goal categories NO23 Kitchen uses (Yağ Yakımı, Kas Kazanımı, ...).
	@GetMapping
	public ResponseEntity<List<SuccessStory>> getPublishedStories(
			@RequestParam(required = false) SuccessCategory category) {
		return ResponseEntity.ok(service.getPublishedStories(category));
	}

	// Admin-only - includes unpublished drafts.
	@GetMapping("/all")
	public ResponseEntity<List<SuccessStory>> getAllStories(HttpServletRequest request) {
		ResponseEntity<List<SuccessStory>> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.getAllStories());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessStory> getStory(@PathVariable int id) {
		return ResponseEntity.ok(service.getStory(id));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<SuccessStory>> getStoriesForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getStoriesForUser(userId));
	}

	@PostMapping
	public ResponseEntity<?> createStory(@RequestBody CreateSuccessStoryRequest request, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.createStory(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateStory(@PathVariable int id, @RequestBody CreateSuccessStoryRequest request,
			HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.updateStory(id, request));
	}

	@PostMapping("/{id}/publish")
	public ResponseEntity<?> publish(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.setPublished(id, true));
	}

	@PostMapping("/{id}/unpublish")
	public ResponseEntity<?> unpublish(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.setPublished(id, false));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStory(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		service.deleteStory(id);
		return ResponseEntity.noContent().build();
	}

	// JwtAuthFilter already guarantees a valid token got this far for
	// non-GET requests - this only narrows ADMIN vs. everyone else, since
	// publishing/curating homepage content is an admin-panel action.
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
