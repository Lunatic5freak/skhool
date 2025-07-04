package com.ba.skhool.student.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.student.dto.ResponseDTO;
import com.ba.skhool.student.dto.StudentGradeSubmissionRequestDTO;
import com.ba.skhool.student.dto.StudentSummary;
import com.ba.skhool.student.entity.StudentSubjectPerformance;
import com.ba.skhool.student.manager.GradeManager;

@RestController
@RequestMapping("/grades")
public class GradeController {

	@Autowired
	private GradeManager gradeManager;

	@PostMapping("/submit")
	public ResponseEntity<?> submitGrades(@RequestBody StudentGradeSubmissionRequestDTO request) {
		ResponseDTO response = gradeManager.saveAll(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/summary")
	public ResponseEntity<?> getSummary(@RequestParam String studentClass,
			@RequestParam(name = "section", required = false) String section,
			@RequestParam(name = "term", required = false) String term) {
		List<StudentSubjectPerformance> performances = gradeManager.findByStudentClassAndSection(term, studentClass,
				section);

		// Group by Student
		Map<String, StudentSummary> studentMap = new HashMap<>();
		for (StudentSubjectPerformance p : performances) {
			Long sid = p.getStudent().getId();
			String key = sid.toString().concat("-").concat(p.getStudent().getClassName()).concat("-")
					.concat(p.getStudent().getSection()).concat("-").concat(p.getTerm());
			StudentSummary summary = studentMap.computeIfAbsent(key,
					k -> new StudentSummary(p.getStudent(), p.getPerformanceRemark(), p.getTerm()));
			summary.addSubjectMarks(p.getSubject().getName(), p.getMarksObtained(), p.getMaxMarks());
		}

		return ResponseEntity.ok(studentMap.values());
	}
}
