package org.no23sports.instructorservice.service;

import java.util.List;

import org.no23sports.instructorservice.model.Instructor;
import org.no23sports.instructorservice.repository.InstructorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstructorService {

	@Autowired
	private InstructorRepo repo;

	public Instructor getInstructor(int id) {
		return repo.findById(id).orElse(null);
	}

	public List<Instructor> getAllInstructors() {
		return repo.findAll();
	}

	public List<Instructor> getActiveInstructors() {
		return repo.findByActiveTrue();
	}

	public List<Instructor> getInstructorsForLesson(int lessonId) {
		return repo.findByLessonId(lessonId);
	}

	public void add(Instructor instructor) {
		repo.save(instructor);
	}

	@Transactional
	public Instructor update(int id, Instructor instructor) {
		Instructor existing = repo.findById(id).orElse(instructor);
		existing.setFirstName(instructor.getFirstName());
		existing.setLastName(instructor.getLastName());
		existing.setPhotoUrl(instructor.getPhotoUrl());
		existing.setBio(instructor.getBio());
		existing.setExperienceYears(instructor.getExperienceYears());
		existing.setSpecializations(instructor.getSpecializations());
		existing.setCertifications(instructor.getCertifications());
		existing.setLessonIds(instructor.getLessonIds());
		existing.setActive(instructor.isActive());
		return repo.save(existing);
	}

	public void deleteInstructor(int id) {
		repo.deleteById(id);
	}
}
