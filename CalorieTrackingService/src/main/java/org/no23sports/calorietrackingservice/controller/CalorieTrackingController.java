package org.no23sports.calorietrackingservice.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.no23sports.calorietrackingservice.model.CreateFoodLogRequest;
import org.no23sports.calorietrackingservice.model.CreateWaterLogRequest;
import org.no23sports.calorietrackingservice.model.DailySummaryResponse;
import org.no23sports.calorietrackingservice.model.FoodLogEntry;
import org.no23sports.calorietrackingservice.model.WaterLogEntry;
import org.no23sports.calorietrackingservice.service.CalorieTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Spec section 7 - the "Kalori Takip Paneli" a member sees after logging in:
// bugünkü hedefi, tükettiği öğünler, alınan protein/karbonhidrat/yağ/kalori,
// su tüketimi.
@RestController
@RequestMapping("/kitchen/tracking")
public class CalorieTrackingController {

	@Autowired
	private CalorieTrackingService service;

	// "Tükettiği öğünler" - log one consumed item (from the menu or custom).
	@PostMapping("/meals")
	public ResponseEntity<FoodLogEntry> logFood(@RequestBody CreateFoodLogRequest request) {
		return ResponseEntity.ok(service.logFood(request));
	}

	@GetMapping("/meals/user/{userId}")
	public ResponseEntity<List<FoodLogEntry>> getMealsForDate(@PathVariable UUID userId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(service.getFoodLogForDate(userId, date != null ? date : LocalDate.now()));
	}

	@DeleteMapping("/meals/{id}")
	public ResponseEntity<?> deleteMeal(@PathVariable int id) {
		service.deleteFoodLogEntry(id);
		return ResponseEntity.noContent().build();
	}

	// "Su tüketimi" - log one glass/bottle of water.
	@PostMapping("/water")
	public ResponseEntity<WaterLogEntry> logWater(@RequestBody CreateWaterLogRequest request) {
		return ResponseEntity.ok(service.logWater(request));
	}

	@GetMapping("/water/user/{userId}")
	public ResponseEntity<List<WaterLogEntry>> getWaterForDate(@PathVariable UUID userId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(service.getWaterLogForDate(userId, date != null ? date : LocalDate.now()));
	}

	@DeleteMapping("/water/{id}")
	public ResponseEntity<?> deleteWater(@PathVariable int id) {
		service.deleteWaterLogEntry(id);
		return ResponseEntity.noContent().build();
	}

	// The panel's single "today" call: goal vs. consumed vs. remaining for
	// calories/protein/carbs/fat, plus water, for one date (defaults to
	// today).
	@GetMapping("/summary/user/{userId}")
	public ResponseEntity<DailySummaryResponse> getDailySummary(@PathVariable UUID userId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(service.getDailySummary(userId, date != null ? date : LocalDate.now()));
	}

	// Member panel's "İlerleme grafikleri" - a run of daily summaries across
	// a date range to chart trends over time.
	@GetMapping("/history/user/{userId}")
	public ResponseEntity<List<DailySummaryResponse>> getHistory(@PathVariable UUID userId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return ResponseEntity.ok(service.getHistory(userId, from, to));
	}
}
