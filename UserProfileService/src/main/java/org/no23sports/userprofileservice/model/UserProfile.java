package org.no23sports.userprofileservice.model;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_profiles")
public class UserProfile {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "userid", nullable = false, unique = true)
    private UUID userId;
    private BigDecimal height;
    private Gender gender;
    private BigDecimal weight;
    private byte age;
    @Enumerated(value = EnumType.STRING)
    private Goal goal;
    @Enumerated(value = EnumType.STRING)
    private ActivityLevel activityLevel;
    
	public UserProfile(BigDecimal height, Gender gender, BigDecimal weight, byte age, Goal goal, ActivityLevel activityLevel) {
		this.gender=gender;
		this.height = height;
		this.weight = weight;
		this.age = age;
		this.goal = goal;
		this.activityLevel = activityLevel;
	}
	
	public int getId() {
		return id;
	}
	
	public UUID getUserId() {
		return userId;
	}
	
	public BigDecimal getHeight() {
		return height;
	}
	
	public void setHeight(BigDecimal height) {
		this.height = height;
	}
	
	public BigDecimal getWeight() {
		return weight;
	}
	
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	public byte getAge() {
		return age;
	}
	
	public void age() {
		this.age++;
	}
	
	public ActivityLevel getActivityLevel() {
		return activityLevel;
	}
	
	public void setActivityLevel(ActivityLevel activityLevel) {
		this.activityLevel = activityLevel;
	}
	
	public Goal getGoal() {
		return goal;
	}
	
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	
	public Gender getGender() {
		return gender;
	}
}
