package org.no23sports.reservationservice.model;

// GROUP_LESSON books a spot in a LessonService catalog class (Bootcamp,
// Reformer Pilates, ...); PERSONAL_TRAINING books a 1:1 slot with an
// instructor from InstructorService, matching spec section 3's "Personal
// Training" ("Birebir antrenman") vs. "Grup Dersleri" split.
public enum ReservationType {
	GROUP_LESSON,
	PERSONAL_TRAINING
}
