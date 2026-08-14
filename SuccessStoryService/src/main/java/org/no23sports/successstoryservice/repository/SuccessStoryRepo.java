package org.no23sports.successstoryservice.repository;

import java.util.List;
import java.util.UUID;

import org.no23sports.successstoryservice.model.SuccessCategory;
import org.no23sports.successstoryservice.model.SuccessStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuccessStoryRepo extends JpaRepository<SuccessStory, Integer> {
	List<SuccessStory> findByPublishedTrue();

	List<SuccessStory> findByPublishedTrueAndCategory(SuccessCategory category);

	List<SuccessStory> findByUserId(UUID userId);
}
