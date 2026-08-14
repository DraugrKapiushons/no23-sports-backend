package org.no23sports.faqservice.repository;

import java.util.List;

import org.no23sports.faqservice.model.FaqCategory;
import org.no23sports.faqservice.model.FaqItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqItemRepo extends JpaRepository<FaqItem, Integer> {
	List<FaqItem> findByPublishedTrueOrderByCategoryAscDisplayOrderAsc();

	List<FaqItem> findByPublishedTrueAndCategoryOrderByDisplayOrderAsc(FaqCategory category);

	List<FaqItem> findAllByOrderByCategoryAscDisplayOrderAsc();
}
