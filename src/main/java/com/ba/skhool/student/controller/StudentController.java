package com.ba.skhool.student.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.constants.AttendanceStatus;
import com.ba.skhool.student.dto.AttendanceDayDto;
import com.ba.skhool.student.dto.AttendanceStatDto;
import com.ba.skhool.student.dto.SearchDTO;
import com.ba.skhool.student.dto.StudentDTO;
import com.ba.skhool.student.dto.SubjectTermScoreDto;
import com.ba.skhool.student.entity.Student;
import com.ba.skhool.student.entity.StudentAttendanceBitmap;
import com.ba.skhool.student.manager.StudentManager;
import com.ba.skhool.student.manager.TeacherManager;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private TeacherManager teacherManager;

	@GetMapping("/student/{id}/attendance")
	public ResponseEntity<AttendanceStatDto> getStudentAttendanceGraph(@PathVariable Long id,
			@RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		return ResponseEntity.ok(studentManager.getStudentAttendanceGraph(id, start, end));
	}

	@GetMapping("/{studentId}/performance/subject-wise")
	public Map<String, List<SubjectTermScoreDto>> getSubjectWiseMarks(@PathVariable Long studentId) {
		return studentManager.getSubjectWiseMarks(studentId);
	}

	@GetMapping("/students/{studentId}/performance/overall-distribution")
	public Map<String, Float> getScoreDistribution(@PathVariable Long studentId) {
		return studentManager.getScoreDistribution(studentId);
	}

	@GetMapping("/students/{studentId}/performance/attendance")
	public List<AttendanceDayDto> getAttendanceTrend(@PathVariable Long studentId) {
		StudentAttendanceBitmap bitmapEntity = studentManager.getStudentAttendance(studentId);

		byte[] bitmap = bitmapEntity.getAttendanceBitmap();
		Date startDate = bitmapEntity.getStartDate(); // assumed present

		List<AttendanceDayDto> trend = new ArrayList<>();

		int totalBits = bitmap.length * 8;
		int dayCount = totalBits / 3;

		for (int dayIndex = 0; dayIndex < dayCount; dayIndex++) {
			int bitOffset = dayIndex * 3;
			int byteIndex = bitOffset / 8;
			int bitIndex = bitOffset % 8;

			if (byteIndex >= bitmap.length)
				break;

			int shift = 8 - 3 - bitIndex;
			int mask = 0b111 << shift;

			int statusCode = (bitmap[byteIndex] & mask) >> shift;
			AttendanceStatus status = AttendanceStatus.fromCode(statusCode);

			LocalDate date = LocalDate.ofInstant(startDate.toInstant(), ZoneId.of("Asia/Kolkata")).plusDays(dayIndex);
			trend.add(new AttendanceDayDto(date.toString(), status));
		}

		return trend;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getStudent(@PathVariable("id") Long id) {
		Student student = studentManager.getStudentDetails(id).orElse(null);
		if (student == null) {
			return ResponseEntity.ok(Map.of("status", "not found"));
		}
		StudentDTO studentDto = new StudentDTO();
		BeanUtils.copyProperties(student, studentDto);
		return ResponseEntity.ok(studentDto);
	}

	@PostMapping("/get_students")
	public ResponseEntity<?> getAllStudents(@RequestBody SearchDTO searchDto) {
		Page<Student> students = studentManager.getAllStudents(searchDto);
		List<StudentDTO> studentsDtos = new ArrayList<>();
		students.forEach(student -> {
			StudentDTO studentDto = new StudentDTO();
			BeanUtils.copyProperties(student, studentDto);
			studentsDtos.add(studentDto);

		});
		return ResponseEntity.ok(Map.of("students", studentsDtos, "totalPage", students.getTotalElements(),
				"totalElements", students.getTotalElements()));
	}

}
