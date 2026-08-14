package org.no23sports.paymentservice.service;

import java.util.List;
import java.util.UUID;

import org.no23sports.paymentservice.exception.SavedCardNotFoundException;
import org.no23sports.paymentservice.model.SaveCardRequest;
import org.no23sports.paymentservice.model.SavedCard;
import org.no23sports.paymentservice.repository.SavedCardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyzipay.model.Card;

@Service
public class CardService {

	@Autowired
	private SavedCardRepo savedCardRepo;

	@Autowired
	private IyzicoGateway iyzicoGateway;

	public SavedCard saveCard(SaveCardRequest req, String email) {
		String existingCardUserKey = savedCardRepo.findFirstByUserId(req.getUserId())
				.map(SavedCard::getCardUserKey)
				.orElse(null);

		Card card = iyzicoGateway.saveCard(existingCardUserKey, email, req.getUserId().toString(),
				req.getCardAlias(), req.getCardHolderName(), req.getCardNumber(), req.getExpireMonth(),
				req.getExpireYear());

		SavedCard savedCard = new SavedCard(req.getUserId(), card.getCardUserKey(), card.getCardToken(),
				req.getCardAlias(), card.getLastFourDigits(), card.getCardAssociation(), card.getCardFamily(),
				card.getCardBankName());
		return savedCardRepo.save(savedCard);
	}

	public List<SavedCard> getCardsForUser(UUID userId) {
		return savedCardRepo.findByUserId(userId);
	}

	public void deleteCard(int id) {
		SavedCard card = savedCardRepo.findById(id).orElseThrow(() -> new SavedCardNotFoundException(id));
		iyzicoGateway.deleteCard(card.getCardUserKey(), card.getCardToken());
		savedCardRepo.delete(card);
	}
}
