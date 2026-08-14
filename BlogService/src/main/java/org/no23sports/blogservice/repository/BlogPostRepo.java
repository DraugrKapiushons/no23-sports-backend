package org.no23sports.blogservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.no23sports.blogservice.model.BlogCategory;
import org.no23sports.blogservice.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepo extends JpaRepository<BlogPost, Integer> {
	List<BlogPost> findByPublishedTrue();

	List<BlogPost> findByPublishedTrueAndCategory(BlogCategory category);

	List<BlogPost> findByAuthorId(UUID authorId);

	Optional<BlogPost> findBySlug(String slug);

	boolean existsBySlug(String slug);
}
