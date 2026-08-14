package org.no23sports.paymentservice.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.no23sports.paymentservice.exception.PaymentNotFoundException;
import org.no23sports.paymentservice.model.CheckoutFormResponse;
import org.no23sports.paymentservice.model.InitializeCheckoutRequest;
import org.no23sports.paymentservice.model.Payment;
import org.no23sports.paymentservice.model.PaymentStatus;
import org.no23sports.paymentservice.model.RefundRequest;
import org.no23sports.paymentservice.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyzipay.model.CheckoutForm;
import com.iyzipay.model.CheckoutFormInitialize;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepo paymentRepo;

	@Autowired
	private IyzicoGateway iyzicoGateway;

	public CheckoutFormResponse initializeCheckout(InitializeCheckoutRequest req) {
		String conversationId = UUID.randomUUID().toString();
		String basketId = req.getReferenceType() + "-" + req.getReferenceId();

		Payment payment = new Payment(req.getUserId(), req.getReferenceType(), req.getReferenceId(), conversationId,
				basketId, req.getPrice(), "TRY");
		payment = paymentRepo.save(payment);

		CheckoutFormInitialize initialize = iyzicoGateway.initializeCheckoutForm(req, conversationId, basketId);
		payment.setIyzicoToken(initialize.getToken());
		paymentRepo.save(payment);

		return new CheckoutFormResponse(payment.getId(), initialize.getToken(), initialize.getPaymentPageUrl(),
				initialize.getCheckoutFormContent());
	}

	// Called from the public /payments/callback endpoint that Iyzico redirects
	// to once the member finishes the checkout form. Re-verifies the result
	// against Iyzico's own API rather than trusting anything in the request -
	// a `token` alone can't be forged into a paid status.
	public Payment handleCallback(String token) {
		CheckoutForm result = iyzicoGateway.retrieveCheckoutForm(token, null);

		Payment payment = paymentRepo.findByIyzicoToken(token)
				.orElseThrow(() -> new PaymentNotFoundException("token:" + token));

		if ("SUCCESS".equals(result.getPaymentStatus())) {
			payment.setStatus(PaymentStatus.SUCCESSFUL);
			payment.setIyzicoPaymentId(result.getPaymentId());
			payment.setPaidPrice(result.getPaidPrice());
		} else {
			payment.setStatus(PaymentStatus.FAILED);
			payment.setFailureReason(result.getErrorMessage());
		}
		return paymentRepo.save(payment);
	}

	public Payment refund(int paymentId, RefundRequest req, String ip) {
		Payment payment = getPayment(paymentId);
		if (payment.getStatus() != PaymentStatus.SUCCESSFUL) {
			throw new IllegalStateException("Only successful payments can be refunded (current status: "
					+ payment.getStatus() + ")");
		}

		BigDecimal amount = (req.getAmount() != null) ? req.getAmount() : payment.getPaidPrice();
		iyzicoGateway.refund(payment.getIyzicoPaymentId(), payment.getConversationId(), amount, ip);

		payment.setStatus(PaymentStatus.REFUNDED);
		payment.setFailureReason(req.getReason());
		return paymentRepo.save(payment);
	}

	public List<Payment> getPaymentsForUser(UUID userId) {
		return paymentRepo.findByUserId(userId);
	}

	public Payment getPayment(int id) {
		return paymentRepo.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
	}
}
