package org.no23sports.paymentservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.no23sports.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
	List<Payment> findByUserId(UUID userId);

	Optional<Payment> findByConversationId(String conversationId);

	Optional<Payment> findByIyzicoToken(String iyzicoToken);
}
