package org.no23sports.reservationservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.no23sports.reservationservice.exception.InvalidReservationStateException;
import org.no23sports.reservationservice.exception.ReservationNotFoundException;
import org.no23sports.reservationservice.exception.SlotFullException;
import org.no23sports.reservationservice.model.CreateReservationRequest;
import org.no23sports.reservationservice.model.Reservation;
import org.no23sports.reservationservice.model.ReservationStatus;
import org.no23sports.reservationservice.repository.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

	private static final List<ReservationStatus> HOLDS_A_SLOT =
			List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);

	@Autowired
	private ReservationRepo repo;

	public Reservation book(CreateReservationRequest req) {
		if (req.getCapacity() != null && req.getLessonId() != null && req.getScheduledStart() != null) {
			long taken = repo.countByLessonIdAndScheduledStartAndStatusIn(req.getLessonId(), req.getScheduledStart(),
					HOLDS_A_SLOT);
			if (taken >= req.getCapacity()) {
				throw new SlotFullException(req.getLessonId(), req.getCapacity());
			}
		}

		Reservation reservation = new Reservation(req.getUserId(), req.getReservationType(), req.getLessonId(),
				req.getInstructorId(), req.getScheduledStart(), req.getScheduledEnd(), req.getNotes());
		return repo.save(reservation);
	}

	public Reservation getReservation(int id) {
		return repo.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));
	}

	public List<Reservation> getAllReservations() {
		return repo.findAll();
	}

	public List<Reservation> getReservationsForUser(UUID userId) {
		return repo.findByUserIdOrderByScheduledStartDesc(userId);
	}

	// "Yaklaşan rezervasyonlar" (upcoming reservations) for the member panel -
	// future slots that haven't been cancelled.
	public List<Reservation> getUpcomingReservationsForUser(UUID userId) {
		return repo.findByUserIdAndScheduledStartAfterAndStatusInOrderByScheduledStartAsc(userId, Instant.now(),
				HOLDS_A_SLOT);
	}

	public List<Reservation> getReservationsForInstructor(Integer instructorId) {
		return repo.findByInstructorIdOrderByScheduledStartAsc(instructorId);
	}

	public Reservation reschedule(int id, CreateReservationRequest req) {
		Reservation reservation = getReservation(id);
		requireNotTerminal(reservation, "reschedule");
		reservation.setLessonId(req.getLessonId());
		reservation.setInstructorId(req.getInstructorId());
		reservation.setScheduledStart(req.getScheduledStart());
		reservation.setScheduledEnd(req.getScheduledEnd());
		if (req.getNotes() != null) {
			reservation.setNotes(req.getNotes());
		}
		reservation.touch();
		return repo.save(reservation);
	}

	public Reservation confirm(int id) {
		Reservation reservation = getReservation(id);
		requireNotTerminal(reservation, "confirm");
		reservation.setStatus(ReservationStatus.CONFIRMED);
		return repo.save(reservation);
	}

	public Reservation cancel(int id, String reason) {
		Reservation reservation = getReservation(id);
		requireNotTerminal(reservation, "cancel");
		reservation.cancel(reason);
		return repo.save(reservation);
	}

	public Reservation complete(int id) {
		Reservation reservation = getReservation(id);
		if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
			throw new InvalidReservationStateException(
					"Only a CONFIRMED reservation can be marked COMPLETED (id: " + id + ")");
		}
		reservation.setStatus(ReservationStatus.COMPLETED);
		return repo.save(reservation);
	}

	public Reservation markNoShow(int id) {
		Reservation reservation = getReservation(id);
		if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
			throw new InvalidReservationStateException(
					"Only a CONFIRMED reservation can be marked NO_SHOW (id: " + id + ")");
		}
		reservation.setStatus(ReservationStatus.NO_SHOW);
		return repo.save(reservation);
	}

	public void deleteReservation(int id) {
		Reservation reservation = getReservation(id);
		repo.delete(reservation);
	}

	private void requireNotTerminal(Reservation reservation, String action) {
		ReservationStatus status = reservation.getStatus();
		if (status == ReservationStatus.CANCELLED || status == ReservationStatus.COMPLETED
				|| status == ReservationStatus.NO_SHOW) {
			throw new InvalidReservationStateException(
					"Cannot " + action + " a reservation that is already " + status + " (id: "
							+ reservation.getId() + ")");
		}
	}
}
