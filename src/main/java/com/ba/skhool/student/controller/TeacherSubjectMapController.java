package com.ba.skhool.student.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.student.dto.SubjectRequest;
import com.ba.skhool.student.dto.TeacherSubjectMappingRequest;
import com.ba.skhool.student.manager.TeacherManager;
import com.ba.skhool.student.manager.TeacherSubjectMapManager;

@RestController
@RequestMapping("/teacher-subject-maps")
public class TeacherSubjectMapController {

	@Autowired
	private TeacherSubjectMapManager mappingManager;

	@Autowired
	private TeacherManager teacherManager;

	@PostMapping
	public ResponseEntity<?> createMappings(@RequestBody TeacherSubjectMappingRequest request) {
		return mappingManager.saveMappings(request);
	}

	@GetMapping("/{id}/subjects")
	public ResponseEntity<?> getSubjectsForTeacher(@PathVariable Long id) {
		List<SubjectRequest> subjectMaps = teacherManager.findByTeacherId(id).stream().map(map -> {
			SubjectRequest subject = new SubjectRequest();
			subject.setCode(map.getSubject().getCode());
			subject.setName(map.getSubject().getName());
			subject.setId(map.getSubject().getId());
			return subject;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(subjectMaps);
	}
}
