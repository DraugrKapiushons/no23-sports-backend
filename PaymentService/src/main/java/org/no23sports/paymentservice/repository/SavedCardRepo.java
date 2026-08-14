package org.no23sports.paymentservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.no23sports.paymentservice.model.SavedCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedCardRepo extends JpaRepository<SavedCard, Integer> {
	List<SavedCard> findByUserId(UUID userId);

	// Iyzico issues one cardUserKey per buyer - reuse it on repeat saves
	// instead of registering the member as a brand-new Iyzico buyer each time.
	Optional<SavedCard> findFirstByUserId(UUID userId);
}
