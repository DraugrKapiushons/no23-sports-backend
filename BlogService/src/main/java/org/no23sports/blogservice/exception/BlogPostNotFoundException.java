package org.no23sports.blogservice.exception;

public class BlogPostNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BlogPostNotFoundException(int id) {
		super("Blog post not found with id: " + id);
	}

	public BlogPostNotFoundException(String slug) {
		super("Blog post not found with slug: " + slug);
	}
}
