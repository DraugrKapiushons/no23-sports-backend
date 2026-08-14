package org.no23sports.instructorservice.model;

import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "instructors")
public class Instructor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;
	private String photoUrl;
	private String bio;
	private int experienceYears;
	@ElementCollection
	private List<String> specializations;
	@ElementCollection
	private List<String> certifications;
	// Ids of lessons (from LessonService) this instructor teaches -
	// "Verdiği dersler" in the spec. Kept as a loose reference rather
	// than a foreign key, same pattern MealPlanService uses for menu items.
	@ElementCollection
	private List<Integer> lessonIds;
	private boolean active;

	public Instructor() {}

	public Instructor(String firstName, String lastName, String photoUrl, String bio, int experienceYears,
			List<String> specializations, List<String> certifications, List<Integer> lessonIds, boolean active) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.photoUrl = photoUrl;
		this.bio = bio;
		this.experienceYears = experienceYears;
		this.specializations = specializations;
		this.certifications = certifications;
		this.lessonIds = lessonIds;
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(int experienceYears) {
		this.experienceYears = experienceYears;
	}

	public List<String> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(List<String> specializations) {
		this.specializations = specializations;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public List<Integer> getLessonIds() {
		return lessonIds;
	}

	public void setLessonIds(List<Integer> lessonIds) {
		this.lessonIds = lessonIds;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
