package org.no23sports.lessonservice.repository;

import org.no23sports.lessonservice.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepo extends JpaRepository<Lesson, Integer>{

}
