package org.no23sports.reservationservice.model;

public enum ReservationStatus {
	// Booked but awaiting staff confirmation (e.g. a PT slot the trainer
	// still needs to accept).
	PENDING,
	CONFIRMED,
	CANCELLED,
	COMPLETED,
	NO_SHOW
}
