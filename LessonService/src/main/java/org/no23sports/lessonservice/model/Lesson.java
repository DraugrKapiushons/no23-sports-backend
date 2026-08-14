package org.no23sports.lessonservice.model;

import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Duration;

@Entity
@Table(name = "lessons")
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private Duration duration;
	@Enumerated(EnumType.STRING)
	private Difficulty difficulty;
	private int caloriesBurnt;
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<AgeGroup> suitableFor;
	
	public Lesson(String name, int hours, int minutes, Difficulty difficulty, int caloriesBurnt, List<AgeGroup> suitableFor) {
		this.name = name;
		this.duration = Duration.ofHours(hours).plusMinutes(minutes);
		this.difficulty = difficulty;
		this.caloriesBurnt = caloriesBurnt;
		this.suitableFor = suitableFor;
	}
	
	public Lesson() {}
	
	public String getName() {
		return name;
	}
	
	public Duration getDuration() {
		return duration;
	}
	
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	public int getCaloriesBurnt() {
		return caloriesBurnt;
	}
	
	public List<AgeGroup> getSuitableFor() {
		return suitableFor;
	}

	public int getId() {
		return id;
	}
	
	public void setName(String name) {
	    this.name = name;
	}

	public void setDuration(Duration duration) {
	    this.duration = duration;
	}

	public void setDifficulty(Difficulty difficulty) {
	    this.difficulty = difficulty;
	}

	public void setCaloriesBurnt(int caloriesBurnt) {
	    this.caloriesBurnt = caloriesBurnt;
	}

	public void setSuitableFor(List<AgeGroup> suitableFor) {
	    this.suitableFor = suitableFor;
	}
}
