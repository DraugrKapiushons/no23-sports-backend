package org.no23sports.faqservice.model;

public class CreateFaqItemRequest {
	private String question;
	private String answer;
	private FaqCategory category;
	private Integer displayOrder;
	// Defaults to false (draft) if omitted - see FaqService#createItem.
	private Boolean published;

	public CreateFaqItemRequest() {}

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

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}
}
