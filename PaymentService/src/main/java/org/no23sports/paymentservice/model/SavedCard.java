package org.no23sports.paymentservice.model;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Never stores the PAN/CVC - only what Iyzico's Card Storage API hands back
// (cardUserKey + cardToken), plus display-only metadata for the "my cards"
// list in the member panel.
@Entity
@Table(name = "saved_cards")
public class SavedCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	private String cardUserKey;
	private String cardToken;
	private String cardAlias;
	private String lastFourDigits;
	private String cardAssociation;
	private String cardFamily;
	private String cardBankName;

	private Instant createdAt;

	public SavedCard() {}

	public SavedCard(UUID userId, String cardUserKey, String cardToken, String cardAlias, String lastFourDigits,
			String cardAssociation, String cardFamily, String cardBankName) {
		this.userId = userId;
		this.cardUserKey = cardUserKey;
		this.cardToken = cardToken;
		this.cardAlias = cardAlias;
		this.lastFourDigits = lastFourDigits;
		this.cardAssociation = cardAssociation;
		this.cardFamily = cardFamily;
		this.cardBankName = cardBankName;
		this.createdAt = Instant.now();
	}

	public int getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getCardUserKey() {
		return cardUserKey;
	}

	public String getCardToken() {
		return cardToken;
	}

	public String getCardAlias() {
		return cardAlias;
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public String getCardAssociation() {
		return cardAssociation;
	}

	public String getCardFamily() {
		return cardFamily;
	}

	public String getCardBankName() {
		return cardBankName;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
