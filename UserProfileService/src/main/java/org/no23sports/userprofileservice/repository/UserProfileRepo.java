package org.no23sports.userprofileservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.no23sports.userprofileservice.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfile, Integer>{

	Optional<UserProfile> findByUserId(UUID userId);

}
