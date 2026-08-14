package org.no23sports.blogservice.service;

import java.util.List;
import java.util.UUID;

import org.no23sports.blogservice.exception.BlogPostNotFoundException;
import org.no23sports.blogservice.model.BlogCategory;
import org.no23sports.blogservice.model.BlogPost;
import org.no23sports.blogservice.model.CreateBlogPostRequest;
import org.no23sports.blogservice.repository.BlogPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogPostService {

	@Autowired
	private BlogPostRepo repo;

	public List<BlogPost> getPublishedPosts(BlogCategory category) {
		if (category != null) {
			return repo.findByPublishedTrueAndCategory(category);
		}
		return repo.findByPublishedTrue();
	}

	// Includes drafts - for the admin panel's own listing.
	public List<BlogPost> getAllPosts() {
		return repo.findAll();
	}

	public BlogPost getPost(int id) {
		return repo.findById(id).orElseThrow(() -> new BlogPostNotFoundException(id));
	}

	public BlogPost getPostBySlug(String slug) {
		return repo.findBySlug(slug).orElseThrow(() -> new BlogPostNotFoundException(slug));
	}

	public List<BlogPost> getPostsForAuthor(UUID authorId) {
		return repo.findByAuthorId(authorId);
	}

	public BlogPost createPost(CreateBlogPostRequest req) {
		String slug = resolveSlug(req.getSlug(), req.getTitle(), null);
		BlogPost post = new BlogPost(req.getAuthorId(), req.getAuthorName(), req.getTitle(), slug, req.getExcerpt(),
				req.getContent(), req.getCoverImageUrl(), req.getCategory(), req.getTags(),
				Boolean.TRUE.equals(req.getPublished()));
		return repo.save(post);
	}

	public BlogPost updatePost(int id, CreateBlogPostRequest req) {
		BlogPost post = getPost(id);
		post.setAuthorId(req.getAuthorId());
		post.setAuthorName(req.getAuthorName());
		post.setTitle(req.getTitle());
		post.setSlug(resolveSlug(req.getSlug(), req.getTitle(), post.getSlug()));
		post.setExcerpt(req.getExcerpt());
		post.setContent(req.getContent());
		post.setCoverImageUrl(req.getCoverImageUrl());
		post.setCategory(req.getCategory());
		post.setTags(req.getTags());
		if (req.getPublished() != null) {
			post.setPublished(req.getPublished());
		}
		return repo.save(post);
	}

	public BlogPost setPublished(int id, boolean published) {
		BlogPost post = getPost(id);
		post.setPublished(published);
		return repo.save(post);
	}

	public void deletePost(int id) {
		BlogPost post = getPost(id);
		repo.delete(post);
	}

	// If a slug was given explicitly, use it as-is (already unique-checked
	// by the caller having full control). Otherwise derive one from the
	// title, and only regenerate it if the post doesn't already have one
	// (so editing a post's title later doesn't silently break its URL).
	private String resolveSlug(String explicitSlug, String title, String existingSlug) {
		if (explicitSlug != null && !explicitSlug.isBlank()) {
			return slugify(explicitSlug);
		}
		if (existingSlug != null && !existingSlug.isBlank()) {
			return existingSlug;
		}
		return uniqueSlugify(title);
	}

	private String uniqueSlugify(String title) {
		String base = slugify(title);
		String candidate = base;
		int suffix = 2;
		while (repo.existsBySlug(candidate)) {
			candidate = base + "-" + suffix++;
		}
		return candidate;
	}

	private String slugify(String input) {
		String normalized = input
				.replace("ı", "i").replace("İ", "I")
				.replace("ş", "s").replace("Ş", "S")
				.replace("ğ", "g").replace("Ğ", "G")
				.replace("ü", "u").replace("Ü", "U")
				.replace("ö", "o").replace("Ö", "O")
				.replace("ç", "c").replace("Ç", "C")
				.toLowerCase();
		return normalized.replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
	}
}
