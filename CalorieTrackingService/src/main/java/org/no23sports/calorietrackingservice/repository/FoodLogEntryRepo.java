package org.no23sports.calorietrackingservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.calorietrackingservice.model.FoodLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodLogEntryRepo extends JpaRepository<FoodLogEntry, Integer> {
	List<FoodLogEntry> findByUserIdAndLogDateOrderByLoggedAtAsc(UUID userId, LocalDate logDate);
	List<FoodLogEntry> findByUserIdAndLogDateBetweenOrderByLogDateAscLoggedAtAsc(UUID userId, LocalDate from, LocalDate to);
}
