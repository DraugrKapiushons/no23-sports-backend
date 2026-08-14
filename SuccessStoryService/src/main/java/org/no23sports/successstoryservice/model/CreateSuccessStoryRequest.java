package org.no23sports.successstoryservice.model;

import java.time.LocalDate;
import java.util.UUID;

public class CreateSuccessStoryRequest {
	private UUID userId;
	private String memberDisplayName;
	private String title;
	private String testimonial;
	private SuccessCategory category;
	private String beforePhotoUrl;
	private String afterPhotoUrl;
	private String videoUrl;
	private LocalDate transformationStartDate;
	private LocalDate transformationEndDate;
	// Defaults to false (draft) if omitted - see SuccessStoryService#createStory.
	private Boolean published;

	public CreateSuccessStoryRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getMemberDisplayName() {
		return memberDisplayName;
	}

	public void setMemberDisplayName(String memberDisplayName) {
		this.memberDisplayName = memberDisplayName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTestimonial() {
		return testimonial;
	}

	public void setTestimonial(String testimonial) {
		this.testimonial = testimonial;
	}

	public SuccessCategory getCategory() {
		return category;
	}

	public void setCategory(SuccessCategory category) {
		this.category = category;
	}

	public String getBeforePhotoUrl() {
		return beforePhotoUrl;
	}

	public void setBeforePhotoUrl(String beforePhotoUrl) {
		this.beforePhotoUrl = beforePhotoUrl;
	}

	public String getAfterPhotoUrl() {
		return afterPhotoUrl;
	}

	public void setAfterPhotoUrl(String afterPhotoUrl) {
		this.afterPhotoUrl = afterPhotoUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public LocalDate getTransformationStartDate() {
		return transformationStartDate;
	}

	public void setTransformationStartDate(LocalDate transformationStartDate) {
		this.transformationStartDate = transformationStartDate;
	}

	public LocalDate getTransformationEndDate() {
		return transformationEndDate;
	}

	public void setTransformationEndDate(LocalDate transformationEndDate) {
		this.transformationEndDate = transformationEndDate;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}
}
