package org.no23sports.faqservice.service;

import java.util.List;

import org.no23sports.faqservice.exception.FaqItemNotFoundException;
import org.no23sports.faqservice.model.CreateFaqItemRequest;
import org.no23sports.faqservice.model.FaqCategory;
import org.no23sports.faqservice.model.FaqItem;
import org.no23sports.faqservice.repository.FaqItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaqService {

	@Autowired
	private FaqItemRepo repo;

	public List<FaqItem> getPublishedItems(FaqCategory category) {
		if (category != null) {
			return repo.findByPublishedTrueAndCategoryOrderByDisplayOrderAsc(category);
		}
		return repo.findByPublishedTrueOrderByCategoryAscDisplayOrderAsc();
	}

	// Includes drafts - for the admin panel's own listing.
	public List<FaqItem> getAllItems() {
		return repo.findAllByOrderByCategoryAscDisplayOrderAsc();
	}

	public FaqItem getItem(int id) {
		return repo.findById(id).orElseThrow(() -> new FaqItemNotFoundException(id));
	}

	public FaqItem createItem(CreateFaqItemRequest req) {
		FaqItem item = new FaqItem(req.getQuestion(), req.getAnswer(), req.getCategory(),
				req.getDisplayOrder() != null ? req.getDisplayOrder() : 0,
				Boolean.TRUE.equals(req.getPublished()));
		return repo.save(item);
	}

	public FaqItem updateItem(int id, CreateFaqItemRequest req) {
		FaqItem item = getItem(id);
		item.setQuestion(req.getQuestion());
		item.setAnswer(req.getAnswer());
		item.setCategory(req.getCategory());
		if (req.getDisplayOrder() != null) {
			item.setDisplayOrder(req.getDisplayOrder());
		}
		if (req.getPublished() != null) {
			item.setPublished(req.getPublished());
		} else {
			item.touch();
		}
		return repo.save(item);
	}

	public FaqItem setPublished(int id, boolean published) {
		FaqItem item = getItem(id);
		item.setPublished(published);
		return repo.save(item);
	}

	public void deleteItem(int id) {
		FaqItem item = getItem(id);
		repo.delete(item);
	}
}
