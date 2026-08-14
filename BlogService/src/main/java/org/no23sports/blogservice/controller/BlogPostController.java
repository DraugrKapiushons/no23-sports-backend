package org.no23sports.blogservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.blogservice.model.BlogCategory;
import org.no23sports.blogservice.model.BlogPost;
import org.no23sports.blogservice.model.CreateBlogPostRequest;
import org.no23sports.blogservice.service.BlogPostService;
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
@RequestMapping("/blog")
public class BlogPostController {

	@Autowired
	private BlogPostService service;

	// Public - blog listing page. Optionally filtered by category
	// (Antrenman, Beslenme, Sakatlık Önleme, Tarifler, Motivasyon,
	// Başarı Hikâyeleri, per spec section 10).
	@GetMapping
	public ResponseEntity<List<BlogPost>> getPublishedPosts(@RequestParam(required = false) BlogCategory category) {
		return ResponseEntity.ok(service.getPublishedPosts(category));
	}

	// Admin-only - includes unpublished drafts.
	@GetMapping("/all")
	public ResponseEntity<List<BlogPost>> getAllPosts(HttpServletRequest request) {
		ResponseEntity<List<BlogPost>> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.getAllPosts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<BlogPost> getPost(@PathVariable int id) {
		return ResponseEntity.ok(service.getPost(id));
	}

	// Public - the post detail page is looked up by its URL slug.
	@GetMapping("/slug/{slug}")
	public ResponseEntity<BlogPost> getPostBySlug(@PathVariable String slug) {
		return ResponseEntity.ok(service.getPostBySlug(slug));
	}

	@GetMapping("/author/{authorId}")
	public ResponseEntity<List<BlogPost>> getPostsForAuthor(@PathVariable UUID authorId) {
		return ResponseEntity.ok(service.getPostsForAuthor(authorId));
	}

	@PostMapping
	public ResponseEntity<?> createPost(@RequestBody CreateBlogPostRequest request, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.createPost(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable int id, @RequestBody CreateBlogPostRequest request,
			HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.updatePost(id, request));
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
	public ResponseEntity<?> deletePost(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		service.deletePost(id);
		return ResponseEntity.noContent().build();
	}

	// JwtAuthFilter already guarantees a valid token got this far for
	// non-GET requests - this only narrows ADMIN vs. everyone else, since
	// authoring/curating blog content is an admin-panel action.
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
