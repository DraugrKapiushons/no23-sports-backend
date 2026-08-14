package org.no23sports.kitchensubscriptionservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.kitchensubscriptionservice.exception.SubscriptionNotFoundException;
import org.no23sports.kitchensubscriptionservice.exception.SubscriptionPackageNotFoundException;
import org.no23sports.kitchensubscriptionservice.model.KitchenSubscription;
import org.no23sports.kitchensubscriptionservice.model.KitchenSubscriptionPackage;
import org.no23sports.kitchensubscriptionservice.model.SubscriptionPeriod;
import org.no23sports.kitchensubscriptionservice.model.SubscriptionStatus;
import org.no23sports.kitchensubscriptionservice.repository.KitchenSubscriptionPackageRepo;
import org.no23sports.kitchensubscriptionservice.repository.KitchenSubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KitchenSubscriptionService {

	@Autowired
	private KitchenSubscriptionRepo subscriptionRepo;

	@Autowired
	private KitchenSubscriptionPackageRepo packageRepo;

	public List<KitchenSubscriptionPackage> getAllPackages() {
		return packageRepo.findAll();
	}

	public KitchenSubscriptionPackage getPackage(int id) {
		return packageRepo.findById(id).orElseThrow(() -> new SubscriptionPackageNotFoundException(id));
	}

	public KitchenSubscription subscribe(UUID userId, int packageId) {
		KitchenSubscriptionPackage pkg = getPackage(packageId);
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = calculateEndDate(startDate, pkg.getPeriod());

		KitchenSubscription subscription = new KitchenSubscription(userId, packageId, startDate, endDate,
				SubscriptionStatus.ACTIVE);
		return subscriptionRepo.save(subscription);
	}

	private LocalDate calculateEndDate(LocalDate startDate, SubscriptionPeriod period) {
		return switch (period) {
			case FIVE_DAY -> startDate.plusDays(5);
			case TEN_DAY -> startDate.plusDays(10);
			case TWENTY_DAY -> startDate.plusDays(20);
			case MONTHLY -> startDate.plusMonths(1);
		};
	}

	public List<KitchenSubscription> getSubscriptionsForUser(UUID userId) {
		return subscriptionRepo.findByUserId(userId);
	}

	public KitchenSubscription getSubscription(int id) {
		return subscriptionRepo.findById(id).orElseThrow(() -> new SubscriptionNotFoundException(id));
	}

	public KitchenSubscription cancelSubscription(int id) {
		KitchenSubscription subscription = getSubscription(id);
		subscription.setStatus(SubscriptionStatus.CANCELLED);
		return subscriptionRepo.save(subscription);
	}
}
