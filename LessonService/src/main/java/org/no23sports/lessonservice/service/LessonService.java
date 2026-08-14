package org.no23sports.lessonservice.service;

import java.util.List;

import org.no23sports.lessonservice.model.Lesson;
import org.no23sports.lessonservice.repository.LessonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LessonService {
	
	@Autowired
	private LessonRepo repo;
	

	public Lesson getLesson(int id) {
		return repo.findById(id).orElse(null);
	}

	public void add(Lesson lesson) {
		repo.save(lesson);
	}
	
	@Transactional
	public Lesson update(int id, Lesson lesson) {
		Lesson existing = repo.findById(id).orElse(lesson);
		existing.setName(lesson.getName());
	    existing.setDuration(lesson.getDuration());
	    existing.setDifficulty(lesson.getDifficulty());
	    existing.setCaloriesBurnt(lesson.getCaloriesBurnt());
	    existing.setSuitableFor(lesson.getSuitableFor());
	    return repo.save(existing);
	}

	public void deleteLesson(int id) {
		repo.deleteById(id);
	}

	public List<Lesson> getAllLessons() {
		return repo.findAll();
	}
}
	