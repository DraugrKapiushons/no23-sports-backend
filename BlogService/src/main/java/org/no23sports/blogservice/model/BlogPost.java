package org.no23sports.blogservice.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "blog_posts")
public class BlogPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// Optional - a post can be attributed to a staff/instructor account, or
	// entered purely as curated editorial content with no linked account.
	@Column(name = "author_id")
	private UUID authorId;
	private String authorName;

	private String title;
	// URL-friendly, unique identifier for the post (e.g. "5-isinma-hareketi").
	@Column(unique = true)
	private String slug;
	private String excerpt;
	@Lob
	private String content;
	private String coverImageUrl;

	@Enumerated(EnumType.STRING)
	private BlogCategory category;
	@ElementCollection
	@CollectionTable(name = "blog_post_tags", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "tag")
	private List<String> tags;

	// Drafts are visible in the admin panel only; public listing filters this.
	private boolean published;

	private Instant createdAt;
	private Instant updatedAt;
	private Instant publishedAt;

	public BlogPost() {}

	public BlogPost(UUID authorId, String authorName, String title, String slug, String excerpt, String content,
			String coverImageUrl, BlogCategory category, List<String> tags, boolean published) {
		this.authorId = authorId;
		this.authorName = authorName;
		this.title = title;
		this.slug = slug;
		this.excerpt = excerpt;
		this.content = content;
		this.coverImageUrl = coverImageUrl;
		this.category = category;
		this.tags = tags;
		this.published = published;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
		this.publishedAt = published ? Instant.now() : null;
	}

	public int getId() {
		return id;
	}

	public UUID getAuthorId() {
		return authorId;
	}

	public void setAuthorId(UUID authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}

	public BlogCategory getCategory() {
		return category;
	}

	public void setCategory(BlogCategory category) {
		this.category = category;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		boolean wasPublished = this.published;
		this.published = published;
		this.updatedAt = Instant.now();
		if (published && !wasPublished) {
			this.publishedAt = Instant.now();
		}
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}
}
