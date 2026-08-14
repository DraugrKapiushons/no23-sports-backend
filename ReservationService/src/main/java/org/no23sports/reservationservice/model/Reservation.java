package org.no23sports.reservationservice.model;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations", indexes = {
		@Index(name = "idx_reservations_user_id", columnList = "user_id"),
		@Index(name = "idx_reservations_lesson_slot", columnList = "lesson_id,scheduled_start") })
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Enumerated(EnumType.STRING)
	private ReservationType reservationType;

	// References LessonService's Lesson.id - required for GROUP_LESSON,
	// left null for a PERSONAL_TRAINING slot that isn't tied to a catalog
	// class. Services are loosely coupled by id only, same as elsewhere in
	// this codebase (see MembershipService's Membership.packageId).
	private Integer lessonId;

	// References InstructorService's Instructor.id - required for
	// PERSONAL_TRAINING, optional for an instructor-led group lesson.
	private Integer instructorId;

	private Instant scheduledStart;
	private Instant scheduledEnd;

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	// Member's note when booking (goal for the session, injury caveats, ...).
	private String notes;

	private Instant cancelledAt;
	private String cancellationReason;

	private Instant createdAt;
	private Instant updatedAt;

	public Reservation() {}

	public Reservation(UUID userId, ReservationType reservationType, Integer lessonId, Integer instructorId,
			Instant scheduledStart, Instant scheduledEnd, String notes) {
		this.userId = userId;
		this.reservationType = reservationType;
		this.lessonId = lessonId;
		this.instructorId = instructorId;
		this.scheduledStart = scheduledStart;
		this.scheduledEnd = scheduledEnd;
		this.notes = notes;
		this.status = ReservationStatus.PENDING;
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public ReservationType getReservationType() {
		return reservationType;
	}

	public void setReservationType(ReservationType reservationType) {
		this.reservationType = reservationType;
	}

	public Integer getLessonId() {
		return lessonId;
	}

	public void setLessonId(Integer lessonId) {
		this.lessonId = lessonId;
	}

	public Integer getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(Integer instructorId) {
		this.instructorId = instructorId;
	}

	public Instant getScheduledStart() {
		return scheduledStart;
	}

	public void setScheduledStart(Instant scheduledStart) {
		this.scheduledStart = scheduledStart;
	}

	public Instant getScheduledEnd() {
		return scheduledEnd;
	}

	public void setScheduledEnd(Instant scheduledEnd) {
		this.scheduledEnd = scheduledEnd;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
		this.updatedAt = Instant.now();
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Instant getCancelledAt() {
		return cancelledAt;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void cancel(String reason) {
		this.status = ReservationStatus.CANCELLED;
		this.cancellationReason = reason;
		this.cancelledAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void touch() {
		this.updatedAt = Instant.now();
	}
}
