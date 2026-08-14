package org.no23sports.mealplanservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.no23sports.mealplanservice.exception.KitchenPlanGenerationException;
import org.no23sports.mealplanservice.model.DietaryTag;
import org.no23sports.mealplanservice.model.Goal;
import org.no23sports.mealplanservice.model.KitchenMealPlan;
import org.no23sports.mealplanservice.model.MealType;
import org.no23sports.mealplanservice.model.MenuCategory;
import org.no23sports.mealplanservice.model.MenuItemDto;
import org.springframework.stereotype.Service;

// Unchanged from the monolith's KitchenPlanMatchingService except that it now
// matches against MenuItemDto (menu-service's response shape) instead of the
// JPA-owned KitchenMenuItem — the scoring/backtracking logic itself didn't
// need to change to make this service independently deployable.
//
// For each of the 5 meal slots, shortlists the menu items that best fit that
// slot's share of the daily targets, then — day by day — searches those
// shortlists for the combination that best matches the plan's daily calorie
// and macro targets, without reusing a menu item from the previous day.
@Service
public class KitchenPlanMatchingService {

	private static final int MAX_QUANTITY_PER_SLOT = 3;
	private static final int SLOT_CANDIDATE_LIMIT = 12;
	private static final List<MealType> PLAN_SLOTS = List.of(MealType.BREAKFAST, MealType.MORNING_SNACK,
			MealType.LUNCH, MealType.AFTERNOON_SNACK, MealType.DINNER);

	public List<PlanDayMatch> generate(KitchenMealPlan plan, LocalDate startDate, int numberOfDays,
			List<MenuItemDto> menuItems) {

		if (numberOfDays <= 0) {
			throw new KitchenPlanGenerationException("Kitchen aboneliğinin tarih aralığı geçerli değil.");
		}

		List<List<MealCandidate>> candidatesBySlot = buildCandidatesBySlot(plan, menuItems);

		List<PlanDayMatch> days = new ArrayList<>();
		Set<Integer> previousMenuItemIds = Set.of();

		for (int dayIndex = 0; dayIndex < numberOfDays; dayIndex++) {
			List<MealCandidate> best = findBestCombination(candidatesBySlot, plan, previousMenuItemIds);

			if (best == null) {
				throw new KitchenPlanGenerationException(
						"Aynı gün içinde tekrar etmeyen öğünlerden oluşan Kitchen planı için yeterli ürün bulunamadı.");
			}

			days.add(buildDayMatch(dayIndex + 1, startDate.plusDays(dayIndex), best));
			previousMenuItemIds = best.stream().map(candidate -> candidate.item().getId())
					.collect(Collectors.toSet());
		}

		return days;
	}

	private List<List<MealCandidate>> buildCandidatesBySlot(KitchenMealPlan plan, List<MenuItemDto> menuItems) {
		List<List<MealCandidate>> candidatesBySlot = new ArrayList<>();

		for (MealType slot : PLAN_SLOTS) {
			MenuCategory category = mapSlotToCategory(slot);

			List<MealCandidate> slotCandidates = menuItems.stream()
					.filter(item -> item.getCategory() == category)
					.flatMap(item -> IntStream.rangeClosed(1, MAX_QUANTITY_PER_SLOT)
							.mapToObj(quantity -> new MealCandidate(item, slot, quantity)))
					.sorted(Comparator.comparingDouble((MealCandidate candidate) -> getSlotScore(candidate, plan))
							.thenComparing(candidate -> candidate.item().getName()))
					.limit(SLOT_CANDIDATE_LIMIT)
					.toList();

			if (slotCandidates.isEmpty()) {
				throw new KitchenPlanGenerationException(
						getSlotDisplayName(slot) + " için plana uygun Kitchen ürünü bulunamadı.");
			}

			candidatesBySlot.add(slotCandidates);
		}

		return candidatesBySlot;
	}

	// Backtracking search over the (small, pre-shortlisted) candidate lists per
	// slot. Pruning on duplicate menu items as we go is equivalent to — but far
	// cheaper than — generating the full cartesian product and filtering it
	// afterwards, which is what the original LINQ implementation did.
	private List<MealCandidate> findBestCombination(List<List<MealCandidate>> candidatesBySlot,
			KitchenMealPlan plan, Set<Integer> previousMenuItemIds) {
		BestCombinationHolder best = new BestCombinationHolder();
		search(candidatesBySlot, 0, new ArrayList<>(), new HashSet<>(), plan, previousMenuItemIds, best);
		return best.combination;
	}

	private void search(List<List<MealCandidate>> candidatesBySlot, int slotIndex, List<MealCandidate> current,
			Set<Integer> usedMenuItemIds, KitchenMealPlan plan, Set<Integer> previousMenuItemIds,
			BestCombinationHolder best) {

		if (slotIndex == candidatesBySlot.size()) {
			double score = getDailyScore(current, plan, previousMenuItemIds);
			BigDecimal calories = sumTotalCalories(current);

			if (best.combination == null || score < best.score
					|| (score == best.score && calories.compareTo(best.calories) < 0)) {
				best.score = score;
				best.calories = calories;
				best.combination = new ArrayList<>(current);
			}
			return;
		}

		for (MealCandidate candidate : candidatesBySlot.get(slotIndex)) {
			int itemId = candidate.item().getId();
			if (usedMenuItemIds.contains(itemId)) {
				continue;
			}

			current.add(candidate);
			usedMenuItemIds.add(itemId);

			search(candidatesBySlot, slotIndex + 1, current, usedMenuItemIds, plan, previousMenuItemIds, best);

			usedMenuItemIds.remove(itemId);
			current.remove(current.size() - 1);
		}
	}

	private static final class BestCombinationHolder {
		private List<MealCandidate> combination;
		private double score = Double.MAX_VALUE;
		private BigDecimal calories = BigDecimal.ZERO;
	}

	private PlanDayMatch buildDayMatch(int dayNumber, LocalDate planDate, List<MealCandidate> candidates) {
		List<PlanItemMatch> items = candidates.stream()
				.sorted(Comparator.comparing(candidate -> candidate.slot().ordinal()))
				.map(candidate -> new PlanItemMatch(
						candidate.item().getId(),
						candidate.slot(),
						candidate.quantity(),
						candidate.item().getName(),
						candidate.item().getCalories(),
						candidate.item().getProtein(),
						candidate.item().getCarbs(),
						candidate.item().getFat(),
						candidate.item().getPrice()))
				.toList();

		return new PlanDayMatch(
				dayNumber,
				planDate,
				sumTotalCalories(candidates),
				sum(candidates, MealCandidate::totalProtein),
				sum(candidates, MealCandidate::totalCarbs),
				sum(candidates, MealCandidate::totalFat),
				items);
	}

	private double getDailyScore(List<MealCandidate> candidates, KitchenMealPlan plan,
			Set<Integer> previousMenuItemIds) {

		BigDecimal calories = sumTotalCalories(candidates);
		BigDecimal protein = sum(candidates, MealCandidate::totalProtein);
		BigDecimal carbs = sum(candidates, MealCandidate::totalCarbs);
		BigDecimal fat = sum(candidates, MealCandidate::totalFat);

		double calorieScore = getDifferenceRatio(calories, plan.getDailyCalorieTarget()) * 6;
		double proteinScore = getMacroScore(protein, plan.getDailyProteinTarget(), true) * 3;
		double carbScore = getMacroScore(carbs, plan.getDailyCarbTarget(), false);
		double fatScore = getMacroScore(fat, plan.getDailyFatTarget(), false);

		long repetitionCount = candidates.stream()
				.filter(candidate -> previousMenuItemIds.contains(candidate.item().getId()))
				.count();
		double repetitionPenalty = repetitionCount * 0.2;

		double goalBonus = candidates.stream()
				.mapToDouble(candidate -> getGoalBonus(candidate.item(), plan.getGoal()))
				.sum();

		return calorieScore + proteinScore + carbScore + fatScore + repetitionPenalty - goalBonus;
	}

	private double getSlotScore(MealCandidate candidate, KitchenMealPlan plan) {
		BigDecimal slotCalorieTarget = plan.getDailyCalorieTarget().multiply(getSlotCalorieRatio(candidate.slot()));
		BigDecimal slotProteinTarget = plan.getDailyProteinTarget().multiply(getSlotCalorieRatio(candidate.slot()));

		return getDifferenceRatio(candidate.totalCalories(), slotCalorieTarget)
				+ getMacroScore(candidate.totalProtein(), slotProteinTarget, true);
	}

	private double getMacroScore(BigDecimal actual, BigDecimal target, boolean penalizeDeficit) {
		if (target.compareTo(BigDecimal.ZERO) <= 0) {
			return 0;
		}

		if (actual.compareTo(target) < 0) {
			double deficit = target.subtract(actual).divide(target, 10, RoundingMode.HALF_UP).doubleValue();
			return penalizeDeficit ? deficit * 1.5 : deficit;
		}

		double surplus = actual.subtract(target).divide(target, 10, RoundingMode.HALF_UP).doubleValue();
		return surplus * 0.35;
	}

	private double getDifferenceRatio(BigDecimal actual, BigDecimal target) {
		if (target.compareTo(BigDecimal.ZERO) <= 0) {
			return 0;
		}
		return actual.subtract(target).abs().divide(target, 10, RoundingMode.HALF_UP).doubleValue();
	}

	private BigDecimal getSlotCalorieRatio(MealType slot) {
		return switch (slot) {
			case BREAKFAST -> BigDecimal.valueOf(0.20);
			case MORNING_SNACK -> BigDecimal.valueOf(0.10);
			case LUNCH -> BigDecimal.valueOf(0.30);
			case AFTERNOON_SNACK -> BigDecimal.valueOf(0.10);
			case DINNER -> BigDecimal.valueOf(0.30);
		};
	}

	private MenuCategory mapSlotToCategory(MealType slot) {
		return switch (slot) {
			case BREAKFAST -> MenuCategory.BREAKFAST;
			case MORNING_SNACK, AFTERNOON_SNACK -> MenuCategory.SNACK;
			case LUNCH, DINNER -> MenuCategory.MAIN_COURSE;
		};
	}

	private String getSlotDisplayName(MealType slot) {
		return switch (slot) {
			case BREAKFAST -> "Kahvaltı";
			case MORNING_SNACK -> "Ara Öğün 1";
			case LUNCH -> "Öğle Yemeği";
			case AFTERNOON_SNACK -> "Ara Öğün 2";
			case DINNER -> "Akşam Yemeği";
		};
	}

	// Tag vocabulary here (DietaryTag) is narrower than the monolith's free-text
	// tags, so only the directly-equivalent goal/tag pairs get a bonus. Worth
	// revisiting if/when more DietaryTag values are added.
	private double getGoalBonus(MenuItemDto item, Goal goal) {
		List<DietaryTag> tags = item.getDietaryTags();
		if (tags == null || tags.isEmpty()) {
			return 0;
		}

		return switch (goal) {
			case LOSE_BODY_FAT -> tags.contains(DietaryTag.LOW_CALORIE) ? 0.15 : 0;
			case MUSCLE_GAIN -> tags.contains(DietaryTag.HIGH_PROTEIN) ? 0.15 : 0;
			case HEALTHY_LIFESTYLE -> tags.contains(DietaryTag.VEGETARIAN) ? 0.05 : 0;
			case MAINTAIN_BODY_WEIGHT, PERFORMANCE_NUTRITION -> 0;
		};
	}

	private BigDecimal sumTotalCalories(List<MealCandidate> candidates) {
		return sum(candidates, MealCandidate::totalCalories);
	}

	private BigDecimal sum(List<MealCandidate> candidates,
			java.util.function.Function<MealCandidate, BigDecimal> extractor) {
		return candidates.stream().map(extractor).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
