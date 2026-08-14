package org.no23sports.successstoryservice.service;

import java.util.List;
import java.util.UUID;

import org.no23sports.successstoryservice.exception.SuccessStoryNotFoundException;
import org.no23sports.successstoryservice.model.CreateSuccessStoryRequest;
import org.no23sports.successstoryservice.model.SuccessCategory;
import org.no23sports.successstoryservice.model.SuccessStory;
import org.no23sports.successstoryservice.repository.SuccessStoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuccessStoryService {

	@Autowired
	private SuccessStoryRepo repo;

	public List<SuccessStory> getPublishedStories(SuccessCategory category) {
		if (category != null) {
			return repo.findByPublishedTrueAndCategory(category);
		}
		return repo.findByPublishedTrue();
	}

	// Includes drafts - for the admin panel's own listing.
	public List<SuccessStory> getAllStories() {
		return repo.findAll();
	}

	public SuccessStory getStory(int id) {
		return repo.findById(id).orElseThrow(() -> new SuccessStoryNotFoundException(id));
	}

	public List<SuccessStory> getStoriesForUser(UUID userId) {
		return repo.findByUserId(userId);
	}

	public SuccessStory createStory(CreateSuccessStoryRequest req) {
		SuccessStory story = new SuccessStory(req.getUserId(), req.getMemberDisplayName(), req.getTitle(),
				req.getTestimonial(), req.getCategory(), req.getBeforePhotoUrl(), req.getAfterPhotoUrl(),
				req.getVideoUrl(), req.getTransformationStartDate(), req.getTransformationEndDate(),
				Boolean.TRUE.equals(req.getPublished()));
		return repo.save(story);
	}

	public SuccessStory updateStory(int id, CreateSuccessStoryRequest req) {
		SuccessStory story = getStory(id);
		story.setUserId(req.getUserId());
		story.setMemberDisplayName(req.getMemberDisplayName());
		story.setTitle(req.getTitle());
		story.setTestimonial(req.getTestimonial());
		story.setCategory(req.getCategory());
		story.setBeforePhotoUrl(req.getBeforePhotoUrl());
		story.setAfterPhotoUrl(req.getAfterPhotoUrl());
		story.setVideoUrl(req.getVideoUrl());
		story.setTransformationStartDate(req.getTransformationStartDate());
		story.setTransformationEndDate(req.getTransformationEndDate());
		if (req.getPublished() != null) {
			story.setPublished(req.getPublished());
		}
		return repo.save(story);
	}

	public SuccessStory setPublished(int id, boolean published) {
		SuccessStory story = getStory(id);
		story.setPublished(published);
		return repo.save(story);
	}

	public void deleteStory(int id) {
		SuccessStory story = getStory(id);
		repo.delete(story);
	}
}
