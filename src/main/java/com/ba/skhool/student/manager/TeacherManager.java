package com.ba.skhool.student.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ba.skhool.constants.AttendanceStatus;
import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.student.dto.AttendanceStatDto;
import com.ba.skhool.student.dto.AttendanceUpdateDto;
import com.ba.skhool.student.dto.DailyAttendanceDto;
import com.ba.skhool.student.dto.ScheduleDto;
import com.ba.skhool.student.dto.SearchDTO;
import com.ba.skhool.student.dto.TeacherDto;
import com.ba.skhool.student.dto.TeacherYearlyPerformanceDto;
import com.ba.skhool.student.entity.StudentAttendanceBitmap;
import com.ba.skhool.student.entity.Teacher;
import com.ba.skhool.student.entity.TeacherPerformance;
import com.ba.skhool.student.entity.TeachersAttendanceBitMap;
import com.ba.skhool.student.repository.TeacherAttendanceRepository;
import com.ba.skhool.student.repository.TeacherPerformanceRepository;
import com.ba.skhool.student.repository.TeacherRepository;
import com.ba.skhool.student.utils.SortBuilder;
import com.ba.skhool.student.utils.SpecificationBuilder;
import com.ba.skhool.utils.AttendanceUtils;
import com.ba.skhool.utils.Utility;

import jakarta.transaction.Transactional;

@Component
public class TeacherManager {

	Logger LOG = LoggerFactory.getLogger(TeacherManager.class);

	@Autowired
	private TeacherRepository teacherRepo;

	@Autowired
	private TeacherAttendanceRepository teacherAttendanceRepo;

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private TeacherPerformanceRepository teacherPerformanceRepository;

	@Autowired
	private KeycloakUserManager keycloakUserManager;

	@Transactional
	public Teacher save(TeacherDto teacherDto) {
		Teacher teacher = new Teacher();
		BeanUtils.copyProperties(teacherDto, teacher);
		teacher.setCreatedBy(UserSessionContextHolder.getUsername());
		UserDto userDto = new UserDto();
		userDto.setFirstName(teacherDto.getFirstname());
		userDto.setLastName(teacherDto.getLastname());
		userDto.setRoles("teacher");
		userDto.setUserName(teacherDto.getUsername());
		userDto.setTenantId(UserSessionContextHolder.getTenantId());
		boolean isCreated = keycloakUserManager.createUserInKeycloak(userDto);
		if (isCreated) {
			teacherRepo.save(teacher);
			return teacher;
		}
		return null;
	}

	@Transactional
	public List<Teacher> saveTeachers(List<TeacherDto> teacherDtos) {
		List<Teacher> teachers = teacherDtos.stream().map(t -> {
			Teacher teacher = new Teacher();
			BeanUtils.copyProperties(t, teacher);
			return teacher;
		}).toList();
		teacherRepo.saveAll(teachers);
		return teachers;
	}

	@Async
	@Transactional
	public void processTeachers(MultipartFile file, String jobId) throws IOException {
		LOG.debug("started teachers creation with jobid: {}", jobId);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
				CSVParser csvParser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get()
						.parse(reader)) {

			List<Teacher> batch = new ArrayList<>(100);

			for (CSVRecord record : csvParser) {
				Teacher teacher = new Teacher();
				teacher.setFirstname(record.get("firstname"));
				teacher.setLastname(record.get("lastname"));
				teacher.setUsername(record.get("username"));
				teacher.setOrganization(UserSessionContextHolder.getTenantId());
				teacher.setContact(record.get("contact"));
				teacher.setOrganizationEmail(record.get("organizationEmail"));
				teacher.setPersonalEmail(record.get("personalEmail"));
				teacher.setGender(record.get("gender"));
				batch.add(teacher);

				if (batch.size() == 100) {
					teacherRepo.saveAll(batch);
					batch.clear();
				}
			}

			if (!batch.isEmpty()) {
				teacherRepo.saveAll(batch);
			}
		}
	}

	public Teacher getTeacherById(Long id) {
		return teacherRepo.findById(id).get();
	}

	public AttendanceStatDto getTeacherAttendanceGraph(Long teacherId, LocalDate start, LocalDate end) {
		TeachersAttendanceBitMap record = teacherAttendanceRepo.findAttendanceByTeacherId(teacherId);

		if (record == null) {
			return new AttendanceStatDto(); // or throw exception
		}

		byte[] bitmap = record.getAttendanceBitmap();
		LocalDate startDate = LocalDate.ofInstant(record.getStartDate().toInstant(), ZoneId.of("Asia/Kolkata"));

		Map<YearMonth, List<AttendanceStatus>> monthMap = new HashMap<>();
		Map<Integer, List<AttendanceStatus>> yearMap = new HashMap<>();
		List<DailyAttendanceDto> trend = new ArrayList<>();

		int presentCount = 0;
		int countableDays = 0;

		for (int i = 0;; i++) {
			LocalDate current = startDate.plusDays(i);
			if (current.isAfter(end) || i * 3 >= bitmap.length * 8)
				break;
			if (current.isBefore(start))
				continue;

			AttendanceStatus status = AttendanceUtils.readAttendance(bitmap, startDate, current);

			if (status != AttendanceStatus.NOT_MARKED && status != AttendanceStatus.HOLIDAY) {
				countableDays++;
				if (status == AttendanceStatus.PRESENT || status == AttendanceStatus.LATE) {
					presentCount++;
				}
			}

			// Group by month and year
			YearMonth ym = YearMonth.from(current);
			int year = current.getYear();

			monthMap.computeIfAbsent(ym, k -> new ArrayList<>()).add(status);
			yearMap.computeIfAbsent(year, k -> new ArrayList<>()).add(status);

			trend.add(new DailyAttendanceDto(current, status));
		}

		double overallPercentage = countableDays == 0 ? 0.0 : (presentCount * 100.0 / countableDays);

		Map<YearMonth, Double> monthlyPercentage = new HashMap<>();
		for (var entry : monthMap.entrySet()) {
			long total = entry.getValue().stream()
					.filter(s -> s != AttendanceStatus.NOT_MARKED && s != AttendanceStatus.HOLIDAY).count();
			long present = entry.getValue().stream()
					.filter(s -> s == AttendanceStatus.PRESENT || s == AttendanceStatus.LATE).count();
			monthlyPercentage.put(entry.getKey(), total == 0 ? 0.0 : present * 100.0 / total);
		}

		Map<Integer, Double> yearlyPercentage = new HashMap<>();
		for (var entry : yearMap.entrySet()) {
			long total = entry.getValue().stream()
					.filter(s -> s != AttendanceStatus.NOT_MARKED && s != AttendanceStatus.HOLIDAY).count();
			long present = entry.getValue().stream()
					.filter(s -> s == AttendanceStatus.PRESENT || s == AttendanceStatus.LATE).count();
			yearlyPercentage.put(entry.getKey(), total == 0 ? 0.0 : present * 100.0 / total);
		}

		AttendanceStatDto summary = new AttendanceStatDto();
		summary.setOverallPercentage(overallPercentage);
		summary.setMonthlyPercentage(monthlyPercentage);
		summary.setYearlyPercentage(yearlyPercentage);
		summary.setTrend(trend);

		return summary;
	}

	// Placeholder schedules
	public List<ScheduleDto> getTeacherSchedules(Long teacherId) {
		return List.of(new ScheduleDto(teacherId, "Math", "10:00 AM", "Monday", null, null),
				new ScheduleDto(teacherId, "Science", "11:00 AM", "Tuesday", null, null));
	}

	private int parseAttendanceBitmap(byte[] bitmap) {
		int count = 0;
		for (byte b : bitmap) {
			count += Integer.bitCount(b & 0xFF);
		}
		return count; // For example, number of present days
	}

	public List<TeacherYearlyPerformanceDto> getYearlyPerformance(Long teacherId) {
		return teacherPerformanceRepository.findAllByTeacherId(teacherId).stream()
				.map(p -> new TeacherYearlyPerformanceDto(p.getAcademicYear(), p.getAverageResultScore(),
						p.getPassRatePercentage(), p.getTeacherFeedbackScore()))
				.toList();
	}

	@Transactional
	public Teacher updateTeacher(Long id, TeacherDto dto) {
		Teacher teacher = teacherRepo.findById(id).orElse(null);
		Utility.copyNonNullProperties(dto, teacher);
		teacherRepo.save(teacher);
		return teacher;
	}

	@Transactional
	public StudentAttendanceBitmap updateAttendance(Long studentId, AttendanceUpdateDto dto) {
		return studentManager.saveStudentAttendance(studentId, dto);
	}

	public TeacherPerformance getTeacherPerformanceByYearAndId(Long teacherId, String year) {
		return teacherPerformanceRepository.findByTeacherIdAndacademicYear(teacherId, year);
	}

	@Transactional
	public void updateTeacherPerformance(TeacherPerformance performance) {
		teacherPerformanceRepository.save(performance);
	}

	public Teacher findByUsername(String username) {
		return teacherRepo.findByUsername(username);
	}

	public Page<Teacher> getAllTeachers(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPageNumber(), searchDTO.getPageSize(),
				SortBuilder.buildSort(searchDTO));
		SpecificationBuilder<Teacher> specBuilder = new SpecificationBuilder<>();
		Specification<Teacher> spec = specBuilder.build(searchDTO.getFilters());
		return teacherRepo.findAll(spec, pageable);
	}

}
