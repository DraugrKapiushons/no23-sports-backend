package org.no23sports.instructorservice.repository;

import java.util.List;

import org.no23sports.instructorservice.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstructorRepo extends JpaRepository<Instructor, Integer> {
	List<Instructor> findByActiveTrue();

	@Query("select i from Instructor i join i.lessonIds l where l = :lessonId")
	List<Instructor> findByLessonId(@Param("lessonId") int lessonId);
}
