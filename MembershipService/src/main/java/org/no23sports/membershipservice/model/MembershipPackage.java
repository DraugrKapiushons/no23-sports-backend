package org.no23sports.membershipservice.model;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "membership_packages")
public class MembershipPackage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private MembershipTier tier;
	@Enumerated(EnumType.STRING)
	private MembershipPeriod period;
	private String description;
	// Number of group lessons included per week. Null means unlimited
	// (the ELITE tier's "Sınırsız grup dersleri").
	@Column(name = "weekly_lesson_quota")
	private Integer weeklyLessonQuota;
	private BigDecimal price;
	@ElementCollection
	@CollectionTable(name = "membership_package_perks", joinColumns = @JoinColumn(name = "package_id"))
	@Column(name = "perk")
	private List<String> perks;

	public MembershipPackage() {}

	public MembershipPackage(MembershipTier tier, MembershipPeriod period, String description,
			Integer weeklyLessonQuota, BigDecimal price, List<String> perks) {
		this.tier = tier;
		this.period = period;
		this.description = description;
		this.weeklyLessonQuota = weeklyLessonQuota;
		this.price = price;
		this.perks = perks;
	}

	public int getId() {
		return id;
	}

	public MembershipTier getTier() {
		return tier;
	}

	public void setTier(MembershipTier tier) {
		this.tier = tier;
	}

	public MembershipPeriod getPeriod() {
		return period;
	}

	public void setPeriod(MembershipPeriod period) {
		this.period = period;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWeeklyLessonQuota() {
		return weeklyLessonQuota;
	}

	public void setWeeklyLessonQuota(Integer weeklyLessonQuota) {
		this.weeklyLessonQuota = weeklyLessonQuota;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public List<String> getPerks() {
		return perks;
	}

	public void setPerks(List<String> perks) {
		this.perks = perks;
	}
}
