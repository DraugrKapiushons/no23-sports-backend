package org.no23sports.faqservice.controller;

import java.util.List;

import org.no23sports.faqservice.model.CreateFaqItemRequest;
import org.no23sports.faqservice.model.FaqCategory;
import org.no23sports.faqservice.model.FaqItem;
import org.no23sports.faqservice.service.FaqService;
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
@RequestMapping("/faq")
public class FaqController {

	@Autowired
	private FaqService service;

	// Public - the site's FAQ page. Optionally filtered by one of the
	// section-13 topics (Üyelik, Dondurma, Ödeme, Kitchen, Teslimat,
	// Rezervasyon, İptal politikası).
	@GetMapping
	public ResponseEntity<List<FaqItem>> getPublishedItems(@RequestParam(required = false) FaqCategory category) {
		return ResponseEntity.ok(service.getPublishedItems(category));
	}

	// Admin-only - includes unpublished drafts.
	@GetMapping("/all")
	public ResponseEntity<List<FaqItem>> getAllItems(HttpServletRequest request) {
		ResponseEntity<List<FaqItem>> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.getAllItems());
	}

	@GetMapping("/{id}")
	public ResponseEntity<FaqItem> getItem(@PathVariable int id) {
		return ResponseEntity.ok(service.getItem(id));
	}

	@PostMapping
	public ResponseEntity<?> createItem(@RequestBody CreateFaqItemRequest request, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.createItem(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateItem(@PathVariable int id, @RequestBody CreateFaqItemRequest request,
			HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.updateItem(id, request));
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
	public ResponseEntity<?> deleteItem(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		service.deleteItem(id);
		return ResponseEntity.noContent().build();
	}

	// JwtAuthFilter already guarantees a valid token got this far for
	// non-GET requests - this only narrows ADMIN vs. everyone else, since
	// curating FAQ content is an admin-panel action.
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
