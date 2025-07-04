package com.ba.skhool.student.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.student.dto.SubjectRequest;
import com.ba.skhool.student.entity.Subject;
import com.ba.skhool.student.manager.SubjectManager;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {
	private final SubjectManager subjectManager;

	@PostMapping("/")
	public ResponseEntity<?> createSubject(@RequestBody SubjectRequest request) {
		Subject s = subjectManager.createSubject(request);
		if (s == null) {
			return ResponseEntity.ok(Map.of("error", "error in creating subject"));
		}
		return ResponseEntity.ok(s);
	}

	@GetMapping("/")
	public ResponseEntity<?> getAllSubjects() {
		List<Subject> subjects = subjectManager.getSubjects();
		List<SubjectRequest> subjectDtos = subjects.stream().map(s -> {
			SubjectRequest sub = new SubjectRequest();
			BeanUtils.copyProperties(s, sub);
			return sub;
		}).toList();
		return ResponseEntity.ok(subjectDtos);
	}
}