package org.no23sports.membershipservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.no23sports.membershipservice.model.Membership;
import org.no23sports.membershipservice.model.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepo extends JpaRepository<Membership, Integer> {
	List<Membership> findByUserId(UUID userId);
	Optional<Membership> findFirstByUserIdAndStatusOrderByStartDateDesc(UUID userId, MembershipStatus status);
}
