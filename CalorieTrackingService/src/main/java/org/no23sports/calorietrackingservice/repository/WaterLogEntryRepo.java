package org.no23sports.calorietrackingservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.calorietrackingservice.model.WaterLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterLogEntryRepo extends JpaRepository<WaterLogEntry, Integer> {
	List<WaterLogEntry> findByUserIdAndLogDateOrderByLoggedAtAsc(UUID userId, LocalDate logDate);
	List<WaterLogEntry> findByUserIdAndLogDateBetweenOrderByLogDateAscLoggedAtAsc(UUID userId, LocalDate from, LocalDate to);
}
