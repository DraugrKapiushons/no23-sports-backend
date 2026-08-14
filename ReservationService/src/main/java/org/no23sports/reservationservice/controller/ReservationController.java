package org.no23sports.reservationservice.controller;

import java.util.List;
import java.util.UUID;

import org.no23sports.reservationservice.model.CancelReservationRequest;
import org.no23sports.reservationservice.model.CreateReservationRequest;
import org.no23sports.reservationservice.model.Reservation;
import org.no23sports.reservationservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	private ReservationService service;

	// The "Randevu Al" / "Rezervasyon Al" booking action - covers both a
	// group-lesson seat and a Personal Training slot.
	@PostMapping
	public ResponseEntity<?> book(@RequestBody CreateReservationRequest request) {
		return ResponseEntity.ok(service.book(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Reservation> getReservation(@PathVariable int id) {
		return ResponseEntity.ok(service.getReservation(id));
	}

	// Admin panel's reservation system - full list across every member.
	@GetMapping
	public ResponseEntity<List<Reservation>> getAllReservations(HttpServletRequest request) {
		ResponseEntity<List<Reservation>> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.getAllReservations());
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Reservation>> getReservationsForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getReservationsForUser(userId));
	}

	// Member panel's "Yaklaşan rezervasyonlar" widget.
	@GetMapping("/user/{userId}/upcoming")
	public ResponseEntity<List<Reservation>> getUpcomingReservationsForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(service.getUpcomingReservationsForUser(userId));
	}

	// An instructor's schedule - InstructorService.Instructor.id.
	@GetMapping("/instructor/{instructorId}")
	public ResponseEntity<List<Reservation>> getReservationsForInstructor(@PathVariable Integer instructorId) {
		return ResponseEntity.ok(service.getReservationsForInstructor(instructorId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> reschedule(@PathVariable int id, @RequestBody CreateReservationRequest request) {
		return ResponseEntity.ok(service.reschedule(id, request));
	}

	@PostMapping("/{id}/confirm")
	public ResponseEntity<?> confirm(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.confirm(id));
	}

	@PostMapping("/{id}/cancel")
	public ResponseEntity<?> cancel(@PathVariable int id, @RequestBody(required = false) CancelReservationRequest request) {
		String reason = request != null ? request.getReason() : null;
		return ResponseEntity.ok(service.cancel(id, reason));
	}

	@PostMapping("/{id}/complete")
	public ResponseEntity<?> complete(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.complete(id));
	}

	@PostMapping("/{id}/no-show")
	public ResponseEntity<?> markNoShow(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.markNoShow(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReservation(@PathVariable int id, HttpServletRequest httpReq) {
		ResponseEntity<?> forbidden = requireAdmin(httpReq);
		if (forbidden != null) {
			return forbidden;
		}
		service.deleteReservation(id);
		return ResponseEntity.noContent().build();
	}

	// JwtAuthFilter already guarantees a valid token got this far - this
	// only narrows ADMIN vs. everyone else for staff-only actions
	// (confirming, marking complete/no-show, the full cross-member list,
	// hard delete). Booking, rescheduling and cancelling are left open to
	// any authenticated member for their own reservations; enforcing that
	// the id in the token matches the reservation's userId is a gateway/
	// BFF-level concern once that's wired up.
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
