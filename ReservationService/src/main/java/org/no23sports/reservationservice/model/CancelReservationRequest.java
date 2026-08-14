package org.no23sports.reservationservice.model;

public class CancelReservationRequest {
	// Optional - iptal politikası (cancellation policy) reasons, free text
	// for now (e.g. "member request", "sick", "no-show grace period").
	private String reason;

	public CancelReservationRequest() {}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
