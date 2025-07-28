package com.ba.skhool.student.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.student.dto.AttendanceDayDto;
import com.ba.skhool.student.dto.AttendanceStatDto;
import com.ba.skhool.student.dto.AttendanceUpdateDto;
import com.ba.skhool.student.dto.ScheduleDto;
import com.ba.skhool.student.dto.SearchDTO;
import com.ba.skhool.student.dto.TeacherDto;
import com.ba.skhool.student.dto.TeacherYearlyPerformanceDto;
import com.ba.skhool.student.entity.Teacher;
import com.ba.skhool.student.manager.StudentManager;
import com.ba.skhool.student.manager.TeacherManager;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private TeacherManager teacherManager;

	@GetMapping("/{id}")
	public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id) {
		Teacher teacher = teacherManager.getTeacherById(id);
		TeacherDto teacherDto = new TeacherDto();
		BeanUtils.copyProperties(teacher, teacherDto);
		return ResponseEntity.ok(teacherDto);
	}

	@GetMapping("/{id}/attendance")
	public ResponseEntity<AttendanceStatDto> getAttendanceGraph(@PathVariable Long id,
			@RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		return ResponseEntity.ok(teacherManager.getTeacherAttendanceGraph(id, start, end));
	}

	@GetMapping("/{id}/schedule")
	public ResponseEntity<List<ScheduleDto>> getSchedule(@PathVariable Long id) {
		return ResponseEntity.ok(teacherManager.getTeacherSchedules(id));
	}

	@GetMapping("/student/{id}/attendance")
	public ResponseEntity<AttendanceStatDto> getStudentAttendanceGraph(@PathVariable Long id,
			@RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		return ResponseEntity.ok(studentManager.getStudentAttendanceGraph(id, start, end));
	}

	@GetMapping("/{teacherId}/performance/graph")
	public List<TeacherYearlyPerformanceDto> getTeacherGraphData(@PathVariable Long teacherId) {
		return teacherManager.getYearlyPerformance(teacherId);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateTeacher(@PathVariable Long id, @RequestBody TeacherDto dto) {
		teacherManager.updateTeacher(id, dto);
		return ResponseEntity.ok("Teacher updated successfully");
	}

	@PutMapping("/student/{id}/attendance")
	public ResponseEntity<String> updateStudentAttendance(@PathVariable Long id, @RequestBody AttendanceUpdateDto dto) {
		studentManager.saveStudentAttendance(id, dto);
		return ResponseEntity.ok("Attendance updated");
	}

	@PostMapping("/get_teachers")
	public ResponseEntity<?> getAllTeachers(@RequestBody SearchDTO searchDto) {
		Page<Teacher> teacherPage = teacherManager.getAllTeachers(searchDto);
		List<TeacherDto> teacherDtos = new ArrayList<>();
		teacherPage.getContent().forEach(teacher -> {
			TeacherDto teacherDto = new TeacherDto();
			BeanUtils.copyProperties(teacher, teacherDto);
			teacherDtos.add(teacherDto);
		});
		Map<String, Object> res = new HashMap<>();
		res.put("teachers", teacherDtos);
		res.put("totalPages", teacherPage.getTotalPages());
		res.put("totalElements", teacherPage.getTotalElements());
		return ResponseEntity.ok(res);
	}

	@GetMapping("/student/get_attendance")
	public ResponseEntity<?> getAtendanceByClassAndSection(@RequestParam String className, @RequestParam String section,
			@RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		List<AttendanceDayDto> attendances = studentManager.getAttendanceByClassAndSection(className, section,
				date == null ? new Date() : date);
		return ResponseEntity.ok(attendances);

	}

	@PostMapping("/student/update_attendance")
	public ResponseEntity<?> updateStudentsAttendance(@RequestBody List<AttendanceDayDto> attendances,
			@RequestParam("date") LocalDate date) {
		studentManager.updateStudentAttendance(attendances, date);
		return ResponseEntity.ok(Map.of("status", "Success"));
	}

	@GetMapping("/{id}/classes")
	public ResponseEntity<List<Map<String, Object>>> getAssignedClasses(@PathVariable Long id) {
		List<Map<String, Object>> classes = teacherManager.getAssignedClasses(id);
		return ResponseEntity.ok(classes);
	}

	@GetMapping("/{teacherId}/attendance-summary")
	public ResponseEntity<Map<String, Object>> getAttendanceSummary(@PathVariable Long teacherId) {
		Map<String, Object> summary = teacherManager.getAttendanceSummaryForToday(teacherId);
		return ResponseEntity.ok(summary);
	}

	@GetMapping("/count")
	public ResponseEntity<?> getTeacherCount() {
		Long totalTeachers = teacherManager.getTeacherCount();
		return ResponseEntity.ok(Map.of("count", totalTeachers));
	}
}
