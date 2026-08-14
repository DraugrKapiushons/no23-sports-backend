package org.no23sports.instructorservice.controller;

import java.util.List;

import org.no23sports.instructorservice.model.Instructor;
import org.no23sports.instructorservice.service.InstructorService;
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
@RequestMapping("/instructors")
public class InstructorController {

	@Autowired
	private InstructorService service;

	@GetMapping("/{id}")
	public ResponseEntity<Instructor> getInstructor(@PathVariable int id) {
		Instructor instructor = service.getInstructor(id);
		if (instructor == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(instructor);
	}

	@GetMapping
	public List<Instructor> getAllInstructors() {
		return service.getAllInstructors();
	}

	@GetMapping("/active")
	public List<Instructor> getActiveInstructors() {
		return service.getActiveInstructors();
	}

	@GetMapping("/lesson/{lessonId}")
	public List<Instructor> getInstructorsForLesson(@PathVariable int lessonId) {
		return service.getInstructorsForLesson(lessonId);
	}

	@PostMapping
	public ResponseEntity<?> addInstructor(@RequestBody Instructor instructor, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		service.add(instructor);
		return ResponseEntity.ok(instructor);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateInstructor(@PathVariable int id, @RequestBody Instructor instructor,
			HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		return ResponseEntity.ok(service.update(id, instructor));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteInstructor(@PathVariable int id, HttpServletRequest request) {
		ResponseEntity<?> forbidden = requireAdmin(request);
		if (forbidden != null) {
			return forbidden;
		}
		service.deleteInstructor(id);
		return ResponseEntity.noContent().build();
	}

	// JwtAuthFilter already guarantees a valid token got this far for
	// non-GET requests - this only narrows ADMIN vs. everyone else, since
	// managing the trainer roster is an admin-panel action.
	private <T> ResponseEntity<T> requireAdmin(HttpServletRequest request) {
		Object role = request.getAttribute("role");
		if (!"ADMIN".equals(role)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return null;
	}
}
