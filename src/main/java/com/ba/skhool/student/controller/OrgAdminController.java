package com.ba.skhool.student.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.iam.entity.User;
import com.ba.skhool.iam.manager.UserManager;
import com.ba.skhool.student.dto.SearchDTO;
import com.ba.skhool.student.dto.TeacherDto;
import com.ba.skhool.student.dto.UpdateTeacherPerformanceDto;
import com.ba.skhool.student.entity.Teacher;
import com.ba.skhool.student.entity.TeacherPerformance;
import com.ba.skhool.student.manager.StudentManager;
import com.ba.skhool.student.manager.TeacherManager;

@RestController
@RequestMapping("/admin")
public class OrgAdminController {

	Logger LOG = LoggerFactory.getLogger(OrgAdminController.class);

	@Autowired
	private TeacherManager teacherManager;

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private UserManager userManager;

	@PostMapping("/")
	public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
		userDto.setTenantId(
				userDto.getTenantId() != null ? userDto.getTenantId() : UserSessionContextHolder.getTenantId());
		User user = userManager.saveUser(userDto);
		if (user == null) {
			return ResponseEntity.ok(Map.of("status", "Failed to create user"));

		}
		return ResponseEntity.ok(user);

	}

	@PostMapping("/teacher")
	private ResponseEntity<?> createTeacher(@RequestBody TeacherDto teacherDto) {
		Teacher t = teacherManager.save(teacherDto);
		return ResponseEntity.ok(t);
	}

	@PostMapping("/students")
	public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file) {
		String jobId = UUID.randomUUID().toString();
		studentManager.processStudentCsvAsync(file, jobId, UserSessionContextHolder.getTenantId(),
				UserSessionContextHolder.getUsername());
		return ResponseEntity.accepted().body("Upload started. Job ID: " + jobId);
	}

	@PostMapping("/teachers/import")
	public ResponseEntity<String> importTeachers(@RequestParam("file") MultipartFile file) {
		String jobId = UUID.randomUUID().toString();
		try {
			teacherManager.processTeachers(file, jobId);
		} catch (Exception e) {
			LOG.error("error oocuered in import teachers: {}", e);
		}
		return ResponseEntity.accepted().body("Upload started. Job ID: " + jobId);
	}

	@PostMapping("/teachers/{id}/performance")
	public ResponseEntity<String> updateTeacherPerformance(@PathVariable Long id,
			@RequestBody UpdateTeacherPerformanceDto dto, @RequestParam("year") String year) {

		TeacherPerformance performance = teacherManager.getTeacherPerformanceByYearAndId(id, year);

		performance.setAverageResultScore(dto.getAverageResultScore());
		performance.setPassRatePercentage(dto.getPassRatePercentage());
		performance.setTeacherFeedbackScore(dto.getTeacherFeedbackScore());
		performance.setSubjectIds(dto.getSubjectIds());
		performance.setRemarks(dto.getRemarks());

		teacherManager.updateTeacherPerformance(performance);
		return ResponseEntity.ok("Teacher performance updated");
	}

	@PostMapping("/teachers/")
	public ResponseEntity<?> getAllTeachers(@RequestBody SearchDTO searchDto) {
		Page<Teacher> teachers = teacherManager.getAllTeachers(searchDto);
		List<TeacherDto> teacherDtos = teachers.getContent().stream().map(t -> {
			TeacherDto teacher = new TeacherDto();
			BeanUtils.copyProperties(t, teacher);
			return teacher;
		}).toList();
		return ResponseEntity.ok(Map.of("teachers", teacherDtos, "totalPage", teachers.getTotalElements(),
				"totalElements", teachers.getTotalElements()));
	}

}
