package org.no23sports.paymentservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.no23sports.paymentservice.exception.IyzicoException;
import org.no23sports.paymentservice.model.BasketItemRequest;
import org.no23sports.paymentservice.model.InitializeCheckoutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iyzipay.Options;
import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Card;
import com.iyzipay.model.CardInformation;
import com.iyzipay.model.CheckoutForm;
import com.iyzipay.model.CheckoutFormInitialize;
import com.iyzipay.model.Currency;
import com.iyzipay.model.Locale;
import com.iyzipay.model.PaymentGroup;
import com.iyzipay.model.Refund;
import com.iyzipay.request.CreateCardRequest;
import com.iyzipay.request.CreateCheckoutFormInitializeRequest;
import com.iyzipay.request.CreateRefundRequest;
import com.iyzipay.request.DeleteCardRequest;
import com.iyzipay.request.RetrieveCheckoutFormRequest;
import com.iyzipay.request.RetrievePaymentRequest;

// All direct traffic to Iyzico's API goes through here - PaymentService and
// CardService never touch the SDK's request/response objects directly.
@Component
public class IyzicoGateway {

	@Autowired
	private Options iyzicoOptions;

	@Value("${iyzico.callback-url}")
	private String callbackUrl;

	public CheckoutFormInitialize initializeCheckoutForm(InitializeCheckoutRequest req, String conversationId,
			String basketId) {
		CreateCheckoutFormInitializeRequest request = new CreateCheckoutFormInitializeRequest();
		request.setLocale(Locale.TR.getValue());
		request.setConversationId(conversationId);
		request.setPrice(req.getPrice());
		request.setPaidPrice(req.getPaidPrice() != null ? req.getPaidPrice() : req.getPrice());
		request.setCurrency(Currency.TRY.name());
		request.setBasketId(basketId);
		request.setPaymentGroup(PaymentGroup.PRODUCT.name());
		request.setCallbackUrl(callbackUrl);

		Buyer buyer = new Buyer();
		buyer.setId(req.getUserId().toString());
		buyer.setName(req.getBuyerName());
		buyer.setSurname(req.getBuyerSurname());
		buyer.setEmail(req.getBuyerEmail());
		buyer.setGsmNumber(req.getBuyerPhone());
		buyer.setIdentityNumber(blankToPlaceholder(req.getBuyerIdentityNumber()));
		buyer.setRegistrationAddress(req.getBuyerAddress());
		buyer.setIp(req.getBuyerIp());
		buyer.setCity(req.getBuyerCity());
		buyer.setCountry(req.getBuyerCountry());
		request.setBuyer(buyer);

		Address address = new Address();
		address.setContactName(req.getBuyerName() + " " + req.getBuyerSurname());
		address.setCity(req.getBuyerCity());
		address.setCountry(req.getBuyerCountry());
		address.setAddress(req.getBuyerAddress());
		request.setShippingAddress(address);
		request.setBillingAddress(address);

		request.setBasketItems(toIyzicoBasketItems(req.getBasketItems()));

		CheckoutFormInitialize result = CheckoutFormInitialize.create(request, iyzicoOptions);
		if ("failure".equals(result.getStatus())) {
			throw new IyzicoException("Iyzico checkout form initialization failed: " + result.getErrorMessage());
		}
		return result;
	}

	public CheckoutForm retrieveCheckoutForm(String token, String conversationId) {
		RetrieveCheckoutFormRequest request = new RetrieveCheckoutFormRequest();
		request.setLocale(Locale.TR.getValue());
		request.setConversationId(conversationId);
		request.setToken(token);
		return CheckoutForm.retrieve(request, iyzicoOptions);
	}

	// Refunds are keyed off the payment *transaction* id, which lives one
	// level below the payment id on Iyzico's side (one per basket item) -
	// look it up via RetrievePaymentRequest before refunding.
	public Refund refund(String iyzicoPaymentId, String conversationId, BigDecimal amount, String ip) {
		RetrievePaymentRequest retrieveRequest = new RetrievePaymentRequest();
		retrieveRequest.setLocale(Locale.TR.getValue());
		retrieveRequest.setConversationId(conversationId);
		retrieveRequest.setPaymentId(iyzicoPaymentId);
		com.iyzipay.model.Payment iyzicoPayment = com.iyzipay.model.Payment.retrieve(retrieveRequest, iyzicoOptions);

		if (iyzicoPayment.getPaymentItems() == null || iyzicoPayment.getPaymentItems().isEmpty()) {
			throw new IyzicoException("Iyzico payment " + iyzicoPaymentId + " has no refundable items");
		}
		String transactionId = iyzicoPayment.getPaymentItems().get(0).getPaymentTransactionId();

		CreateRefundRequest refundRequest = new CreateRefundRequest();
		refundRequest.setLocale(Locale.TR.getValue());
		refundRequest.setConversationId(conversationId);
		refundRequest.setPaymentTransactionId(transactionId);
		refundRequest.setPrice(amount);
		refundRequest.setIp(ip);

		Refund refund = Refund.create(refundRequest, iyzicoOptions);
		if ("failure".equals(refund.getStatus())) {
			throw new IyzicoException("Iyzico refund failed: " + refund.getErrorMessage());
		}
		return refund;
	}

	public Card saveCard(String existingCardUserKey, String email, String userId, String cardAlias,
			String cardHolderName, String cardNumber, String expireMonth, String expireYear) {
		CardInformation cardInformation = new CardInformation();
		cardInformation.setCardAlias(cardAlias);
		cardInformation.setCardHolderName(cardHolderName);
		cardInformation.setCardNumber(cardNumber);
		cardInformation.setExpireMonth(expireMonth);
		cardInformation.setExpireYear(expireYear);

		CreateCardRequest request = new CreateCardRequest();
		request.setLocale(Locale.TR.getValue());
		request.setConversationId(userId);
		request.setEmail(email);
		request.setExternalId(userId);
		// Omitting cardUserKey makes Iyzico register a brand-new buyer;
		// passing an existing one adds the card to that buyer's card list.
		if (existingCardUserKey != null) {
			request.setCardUserKey(existingCardUserKey);
		}
		request.setCard(cardInformation);

		Card card = Card.create(request, iyzicoOptions);
		if ("failure".equals(card.getStatus())) {
			throw new IyzicoException("Iyzico card save failed: " + card.getErrorMessage());
		}
		return card;
	}

	public void deleteCard(String cardUserKey, String cardToken) {
		DeleteCardRequest request = new DeleteCardRequest();
		request.setLocale(Locale.TR.getValue());
		request.setCardUserKey(cardUserKey);
		request.setCardToken(cardToken);

		Card result = Card.delete(request, iyzicoOptions);
		if ("failure".equals(result.getStatus())) {
			throw new IyzicoException("Iyzico card delete failed: " + result.getErrorMessage());
		}
	}

	private List<BasketItem> toIyzicoBasketItems(List<BasketItemRequest> items) {
		return items.stream().map(item -> {
			BasketItem basketItem = new BasketItem();
			basketItem.setId(item.getId());
			basketItem.setName(item.getName());
			basketItem.setCategory1(item.getCategory());
			basketItem.setItemType(BasketItemType.VIRTUAL.name());
			basketItem.setPrice(item.getPrice());
			return basketItem;
		}).toList();
	}

	private String blankToPlaceholder(String identityNumber) {
		// Iyzico requires a TCKN/identity number even for members who never
		// entered one (e.g. foreign nationals without one on file yet).
		return (identityNumber == null || identityNumber.isBlank()) ? "11111111111" : identityNumber;
	}
}
