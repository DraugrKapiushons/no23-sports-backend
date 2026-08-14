package org.no23sports.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.no23sports.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
	public Optional<User> findByEmailAddress(String email);
	public boolean existsByEmailAddress(String email);
}
