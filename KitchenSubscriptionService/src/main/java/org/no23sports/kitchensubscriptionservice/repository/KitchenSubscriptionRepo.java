package org.no23sports.kitchensubscriptionservice.repository;

import java.util.List;
import java.util.UUID;

import org.no23sports.kitchensubscriptionservice.model.KitchenSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenSubscriptionRepo extends JpaRepository<KitchenSubscription, Integer> {
	List<KitchenSubscription> findByUserId(UUID userId);
}
