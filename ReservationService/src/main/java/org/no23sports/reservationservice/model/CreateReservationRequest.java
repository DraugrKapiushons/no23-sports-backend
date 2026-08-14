package org.no23sports.reservationservice.model;

import java.time.Instant;
import java.util.UUID;

public class CreateReservationRequest {
	private UUID userId;
	private ReservationType reservationType;
	private Integer lessonId;
	private Integer instructorId;
	private Instant scheduledStart;
	private Instant scheduledEnd;
	private String notes;

	// Optional - how many members can be booked into this exact
	// lessonId + scheduledStart slot. Left null (or omitted) skips the
	// capacity check entirely, e.g. for PERSONAL_TRAINING bookings, which
	// are 1:1 by definition.
	private Integer capacity;

	public CreateReservationRequest() {}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
}
