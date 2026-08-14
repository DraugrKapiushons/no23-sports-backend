package org.no23sports.lessonservice.controller;

import java.util.List;

import org.no23sports.lessonservice.model.Lesson;
import org.no23sports.lessonservice.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/lessons")
public class LessonController {

	@Autowired
	private LessonService service;
	
	@GetMapping("/{id}")
	public ResponseEntity<Lesson> getLesson(@PathVariable int id){
		Lesson lesson = service.getLesson(id);
		if (lesson == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(lesson);
	}
	
	@GetMapping
	public List<Lesson> getAllLessons(){
		return service.getAllLessons();
	}
	
	@PostMapping
	public ResponseEntity<?> addLesson(@RequestBody Lesson lesson, HttpServletRequest request){
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		service.add(lesson);
		return ResponseEntity.ok(lesson);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateLesson(@PathVariable int id, @RequestBody Lesson lesson, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.update(id, lesson));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteLesson(@PathVariable int id, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		service.deleteLesson(id);
		return ResponseEntity.noContent().build();
	}

	// JwtAuthFilter already guarantees a valid token got this far for
	// non-GET requests - this only narrows ADMIN vs. everyone else, since
	// scheduling / managing the class timetable is an admin-panel action.
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
