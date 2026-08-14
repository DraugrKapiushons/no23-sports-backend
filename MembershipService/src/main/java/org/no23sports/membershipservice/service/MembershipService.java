package org.no23sports.membershipservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.membershipservice.exception.MembershipNotFoundException;
import org.no23sports.membershipservice.exception.MembershipPackageNotFoundException;
import org.no23sports.membershipservice.exception.NoActiveMembershipException;
import org.no23sports.membershipservice.model.Membership;
import org.no23sports.membershipservice.model.MembershipPackage;
import org.no23sports.membershipservice.model.MembershipPeriod;
import org.no23sports.membershipservice.model.MembershipStatus;
import org.no23sports.membershipservice.repository.MembershipPackageRepo;
import org.no23sports.membershipservice.repository.MembershipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {

	@Autowired
	private MembershipRepo membershipRepo;

	@Autowired
	private MembershipPackageRepo packageRepo;

	public List<MembershipPackage> getAllPackages() {
		return packageRepo.findAll();
	}

	public MembershipPackage getPackage(int id) {
		return packageRepo.findById(id).orElseThrow(() -> new MembershipPackageNotFoundException(id));
	}

	public MembershipPackage addPackage(MembershipPackage pkg) {
		return packageRepo.save(pkg);
	}

	public MembershipPackage updatePackage(int id, MembershipPackage update) {
		MembershipPackage pkg = getPackage(id);
		pkg.setTier(update.getTier());
		pkg.setPeriod(update.getPeriod());
		pkg.setDescription(update.getDescription());
		pkg.setWeeklyLessonQuota(update.getWeeklyLessonQuota());
		pkg.setPrice(update.getPrice());
		pkg.setPerks(update.getPerks());
		return packageRepo.save(pkg);
	}

	public void deletePackage(int id) {
		packageRepo.deleteById(id);
	}

	public Membership subscribe(UUID userId, int packageId, boolean autoRenew) {
		MembershipPackage pkg = getPackage(packageId);
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = calculateEndDate(startDate, pkg.getPeriod());

		Membership membership = new Membership(userId, packageId, startDate, endDate, MembershipStatus.ACTIVE,
				autoRenew);
		return membershipRepo.save(membership);
	}

	private LocalDate calculateEndDate(LocalDate startDate, MembershipPeriod period) {
		return switch (period) {
			case MONTHLY -> startDate.plusMonths(1);
			case QUARTERLY -> startDate.plusMonths(3);
			case ANNUAL -> startDate.plusYears(1);
		};
	}

	public List<Membership> getMembershipsForUser(UUID userId) {
		return membershipRepo.findByUserId(userId);
	}

	public Membership getActiveMembership(UUID userId) {
		return membershipRepo.findFirstByUserIdAndStatusOrderByStartDateDesc(userId, MembershipStatus.ACTIVE)
				.orElseThrow(() -> new NoActiveMembershipException(userId));
	}

	public Membership getMembership(int id) {
		return membershipRepo.findById(id).orElseThrow(() -> new MembershipNotFoundException(id));
	}

	public Membership cancelMembership(int id) {
		Membership membership = getMembership(id);
		membership.setStatus(MembershipStatus.CANCELLED);
		membership.setAutoRenew(false);
		return membershipRepo.save(membership);
	}

	public Membership pauseMembership(int id) {
		Membership membership = getMembership(id);
		membership.setStatus(MembershipStatus.PAUSED);
		return membershipRepo.save(membership);
	}

	public Membership resumeMembership(int id) {
		Membership membership = getMembership(id);
		membership.setStatus(MembershipStatus.ACTIVE);
		return membershipRepo.save(membership);
	}
}
