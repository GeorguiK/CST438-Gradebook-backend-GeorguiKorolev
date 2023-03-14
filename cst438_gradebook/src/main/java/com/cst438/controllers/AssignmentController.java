package com.cst438.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
public class AssignmentController {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/assignment/{id}")
	public AssignmentDTO assignment(@PathVariable("id") Integer assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
		if(assignment != null) return assignment.toDTO();
		System.out.println("Error: No Assignment by that id");
		return null;
	}
	
	//adds an assignment
	@PutMapping("/addAssignment")
	@Transactional
	public AssignmentDTO addAssignment (@RequestBody AssignmentDTO assignmentDTO) {
		
		Assignment assignment = new Assignment();
		assignment.setDueDate(Date.valueOf(assignmentDTO.dueDate));
		assignment.setName(assignmentDTO.name);
		assignment.setNeedsGrading(assignmentDTO.needsGrading);
		
		Course course = courseRepository.findById(assignmentDTO.courseId).orElse(null);
		assignment.setCourse(course);
		if(course != null) {
			course.getAssignments().add(assignment);
			assignmentRepository.save(assignment);
			return assignment.toDTO();
		} else {
			System.out.println("Error: No Course by that id");
			return null;
		}
		
	}
	
	@PutMapping("/renameAssignment")
	@Transactional
	public AssignmentDTO renameAssignment (@RequestBody AssignmentDTO assignmentDTO) {
		
		Assignment assignment = assignmentRepository.findById(assignmentDTO.assignmentId).orElse(null);
		if(assignment != null) {
			assignment.setName(assignmentDTO.name);
			assignmentRepository.save(assignment);
			return assignment.toDTO();
		}
		System.out.println("Error: No Assignment by that id");
		return null;
	}
	
	@DeleteMapping("/deleteAssignment")
	@Transactional
	public AssignmentDTO deleteAssignment (@RequestBody AssignmentDTO assignmentDTO) {
		Assignment assignment = assignmentRepository.findById(assignmentDTO.assignmentId).orElse(null);
		if(assignment.getGrades() == null || assignment.getGrades().isEmpty()) {
			assignmentRepository.deleteById(assignment.getId());
			return assignment.toDTO();
		}
		System.out.println("Error: cannot delete graded assignments");
		return null;
	}
}
