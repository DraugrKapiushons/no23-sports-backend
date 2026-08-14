package org.no23sports.faqservice.model;

import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "faq_items")
public class FaqItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String question;

	@Lob
	private String answer;

	@Enumerated(EnumType.STRING)
	private FaqCategory category;

	// Controls ordering within a category on the public FAQ page - lower
	// values are shown first. Ties fall back to id order.
	private int displayOrder;

	// Drafts are visible in the admin panel only; public listing filters this.
	private boolean published;

	private Instant createdAt;
	private Instant updatedAt;

	public FaqItem() {}

	public FaqItem(String question, String answer, FaqCategory category, int displayOrder, boolean published) {
		this.question = question;
		this.answer = answer;
		this.category = category;
		this.displayOrder = displayOrder;
		this.published = published;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	public int getId() {
		return id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public FaqCategory getCategory() {
		return category;
	}

	public void setCategory(FaqCategory category) {
		this.category = category;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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

	public void touch() {
		this.updatedAt = Instant.now();
	}
}
