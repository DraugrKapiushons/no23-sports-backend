package org.no23sports.paymentservice.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.no23sports.paymentservice.model.CheckoutFormResponse;
import org.no23sports.paymentservice.model.InitializeCheckoutRequest;
import org.no23sports.paymentservice.model.Payment;
import org.no23sports.paymentservice.model.PaymentStatus;
import org.no23sports.paymentservice.model.RefundRequest;
import org.no23sports.paymentservice.model.SaveCardRequest;
import org.no23sports.paymentservice.model.SavedCard;
import org.no23sports.paymentservice.service.CardService;
import org.no23sports.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CardService cardService;

	@Value("${frontend.payment-success-url}")
	private String successUrl;

	@Value("${frontend.payment-failure-url}")
	private String failureUrl;

	@PostMapping("/checkout-form")
	public ResponseEntity<CheckoutFormResponse> initializeCheckout(@RequestBody InitializeCheckoutRequest request) {
		return ResponseEntity.ok(paymentService.initializeCheckout(request));
	}

	// Iyzico calls this itself (browser redirect with a `token` form field) -
	// see JwtAuthFilter, which leaves this one path open. It always redirects
	// back to the frontend rather than returning JSON, since the caller here
	// is the member's browser, not our own frontend's fetch client.
	@RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Void> callback(@RequestParam("token") String token) {
	    Payment payment = paymentService.handleCallback(token);
	    String redirectBase = (payment.getStatus() == PaymentStatus.SUCCESSFUL)
	            ? successUrl
	            : failureUrl;
	    return ResponseEntity.status(HttpStatus.FOUND)
	            .location(URI.create(redirectBase + "?paymentId=" + payment.getId()))
	            .build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Payment> getPayment(@PathVariable int id) {
		return ResponseEntity.ok(paymentService.getPayment(id));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Payment>> getPaymentsForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(paymentService.getPaymentsForUser(userId));
	}

	@PostMapping("/{id}/refund")
	public ResponseEntity<Payment> refund(@PathVariable int id, @RequestBody RefundRequest request,
			jakarta.servlet.http.HttpServletRequest httpRequest) {
		return ResponseEntity.ok(paymentService.refund(id, request, httpRequest.getRemoteAddr()));
	}

	@PostMapping("/cards")
	public ResponseEntity<SavedCard> saveCard(@RequestBody SaveCardRequest request,
			jakarta.servlet.http.HttpServletRequest httpRequest) {
		String email = String.valueOf(httpRequest.getAttribute("email"));
		return ResponseEntity.ok(cardService.saveCard(request, email));
	}

	@GetMapping("/cards/user/{userId}")
	public ResponseEntity<List<SavedCard>> getCardsForUser(@PathVariable UUID userId) {
		return ResponseEntity.ok(cardService.getCardsForUser(userId));
	}

	@DeleteMapping("/cards/{id}")
	public ResponseEntity<Void> deleteCard(@PathVariable int id) {
		cardService.deleteCard(id);
		return ResponseEntity.noContent().build();
	}
}
