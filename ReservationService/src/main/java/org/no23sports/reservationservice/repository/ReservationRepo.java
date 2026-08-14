package org.no23sports.reservationservice.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.no23sports.reservationservice.model.Reservation;
import org.no23sports.reservationservice.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
	List<Reservation> findByUserIdOrderByScheduledStartDesc(UUID userId);

	List<Reservation> findByUserIdAndScheduledStartAfterAndStatusInOrderByScheduledStartAsc(UUID userId,
			Instant after, List<ReservationStatus> statuses);

	List<Reservation> findByInstructorIdOrderByScheduledStartAsc(Integer instructorId);

	long countByLessonIdAndScheduledStartAndStatusIn(Integer lessonId, Instant scheduledStart,
			List<ReservationStatus> statuses);
}
