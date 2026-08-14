package org.no23sports.successstoryservice.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "success_stories")
public class SuccessStory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// Optional - a story can be linked back to the member's account (so it
	// can show up on their own profile/panel) or entered purely as curated
	// marketing content with no linked account at all.
	@Column(name = "user_id")
	private UUID userId;

	private String memberDisplayName;

	private String title;

	@Lob
	private String testimonial;

	@Enumerated(EnumType.STRING)
	private SuccessCategory category;

	private String beforePhotoUrl;
	private String afterPhotoUrl;
	private String videoUrl;

	private LocalDate transformationStartDate;
	private LocalDate transformationEndDate;

	// Drafts are visible in the admin panel only; public listing filters this.
	private boolean published;

	private Instant createdAt;
	private Instant updatedAt;

	public SuccessStory() {}

	public SuccessStory(UUID userId, String memberDisplayName, String title, String testimonial,
			SuccessCategory category, String beforePhotoUrl, String afterPhotoUrl, String videoUrl,
			LocalDate transformationStartDate, LocalDate transformationEndDate, boolean published) {
		this.userId = userId;
		this.memberDisplayName = memberDisplayName;
		this.title = title;
		this.testimonial = testimonial;
		this.category = category;
		this.beforePhotoUrl = beforePhotoUrl;
		this.afterPhotoUrl = afterPhotoUrl;
		this.videoUrl = videoUrl;
		this.transformationStartDate = transformationStartDate;
		this.transformationEndDate = transformationEndDate;
		this.published = published;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	public int getId() {
		return id;
	}

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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
		this.updatedAt = Instant.now();
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
