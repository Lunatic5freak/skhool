package com.ba.skhool.student.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.student.dto.ResponseDTO;
import com.ba.skhool.student.dto.SchoolClassDTO;
import com.ba.skhool.student.entity.ClassSubjectMap;
import com.ba.skhool.student.entity.SchoolClass;
import com.ba.skhool.student.manager.SchoolClassManager;

@RestController
@RequestMapping("/class")
public class ClassController {

	@Autowired
	private SchoolClassManager classManager;

	@GetMapping("/")
	public ResponseEntity<?> getClasses() {
		List<SchoolClass> classes = classManager.getAllClasses();
		List<SchoolClassDTO> classDtos = classes.stream().map(c -> {
			SchoolClassDTO classDto = new SchoolClassDTO();
			BeanUtils.copyProperties(c, classDto);
			return classDto;
		}).toList();
		return ResponseEntity.ok(classDtos);
	}

	@PostMapping("/")
	public ResponseEntity<?> createLasses(List<SchoolClassDTO> classDtos) {
		ResponseDTO res = classManager.saveClasses(classDtos);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/mapped-subject")
	public ResponseEntity<?> getClassVsSubject(@RequestParam("classId") Long classId) {
		List<ClassSubjectMap> classVsSubjects = classManager.getClassVsSubject(classId);
		return ResponseEntity.ok(classVsSubjects);
	}
}
