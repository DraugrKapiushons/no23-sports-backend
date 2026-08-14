package org.no23sports.paymentservice.model;

// What the payment is for. referenceId on Payment points back at the owning
// record in that domain's own service (e.g. a KitchenSubscription id) -
// PaymentService doesn't own or validate that record, it just tags it.
public enum PaymentReferenceType {
	MEMBERSHIP_PACKAGE,
	KITCHEN_SUBSCRIPTION,
	KITCHEN_ORDER,
	SHOP_ORDER,
	EVENT
}
