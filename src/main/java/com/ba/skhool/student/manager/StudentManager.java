package com.ba.skhool.student.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ba.skhool.constants.AttendanceStatus;
import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.student.dto.AttendanceDayDto;
import com.ba.skhool.student.dto.AttendanceStatDto;
import com.ba.skhool.student.dto.AttendanceUpdateDto;
import com.ba.skhool.student.dto.DailyAttendanceDto;
import com.ba.skhool.student.dto.SearchDTO;
import com.ba.skhool.student.dto.StudentDTO;
import com.ba.skhool.student.dto.SubjectTermScoreDto;
import com.ba.skhool.student.entity.Student;
import com.ba.skhool.student.entity.StudentAttendanceBitmap;
import com.ba.skhool.student.entity.StudentOverallPerformance;
import com.ba.skhool.student.entity.StudentSubjectPerformance;
import com.ba.skhool.student.repository.StudentAttendanceRepository;
import com.ba.skhool.student.repository.StudentOverAllPerformanceRepository;
import com.ba.skhool.student.repository.StudentPerformanceRepository;
import com.ba.skhool.student.repository.StudentRepository;
import com.ba.skhool.student.utils.SortBuilder;
import com.ba.skhool.student.utils.SpecificationBuilder;
import com.ba.skhool.utils.AttendanceUtils;
import com.example.multitenant.context.TenantContext;

import jakarta.transaction.Transactional;

@Component
public class StudentManager {

	Logger LOG = LoggerFactory.getLogger(StudentManager.class);

//	private final JdbcTemplate jdbcTemplate;
	private final TaskExecutor taskExecutor;

	private final StudentRepository studentRepository;
	private final StudentPerformanceRepository studentPerformanceRepo;
	private final StudentOverAllPerformanceRepository studentOverAllPerformanceRepo;
	private final StudentAttendanceRepository studentAttendanceRepo;
	private final KeycloakUserManager keycloakUserManager;

	public StudentManager(TaskExecutor taskExecutor, StudentRepository studentRepository,
			StudentPerformanceRepository studentPerformanceRepo,
			StudentOverAllPerformanceRepository studentOverAllPerformanceRepo,
			StudentAttendanceRepository studentAttendanceRepo, KeycloakUserManager keycloakUserManager) {
		this.taskExecutor = taskExecutor;
		this.studentRepository = studentRepository;
		this.studentPerformanceRepo = studentPerformanceRepo;
		this.studentOverAllPerformanceRepo = studentOverAllPerformanceRepo;
		this.studentAttendanceRepo = studentAttendanceRepo;
		this.keycloakUserManager = keycloakUserManager;
	}

	@Transactional
	public Student save(StudentDTO studentDto) {
		Student student = new Student();
		BeanUtils.copyProperties(studentDto, student);
		student.setOrganization(UserSessionContextHolder.getTenantId());
		student.setCreatedBy(UserSessionContextHolder.getUsername());
		UserDto userDto = new UserDto();
		userDto.setFirstname(studentDto.getFirstname());
		userDto.setLastname(studentDto.getLastname());
		userDto.setRoles("student");
		userDto.setUsername(studentDto.getUsername());
		userDto.setTenantId(UserSessionContextHolder.getTenantId());
		userDto.setId(student.getId());
		boolean isCreated = keycloakUserManager.createUserInKeycloak(userDto);
		if (isCreated) {
			studentRepository.save(student);
			return student;
		}
		return null;
	}

	@Async
	@Transactional
	public void processStudentCsvAsync(MultipartFile file, String jobId, Long orgId, String userName) {
		LOG.debug("Started process with jobId: {}", jobId);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		taskExecutor.execute(() -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get()
						.parse(reader);
				List<Student> batch = new ArrayList<>();
				TenantContext.setTenantId(orgId);
				for (CSVRecord record : parser) {
					Student student = new Student();
					student.setClassName(record.get("classname"));
					student.setFirstname(record.get("firstname"));
					student.setLastname(record.get("lastname"));
					student.setOrganization(orgId);
					student.setCreatedBy(userName);
					student.setOrganizationEmail(record.get("organization email"));
					student.setGuardian(record.get("guardian"));
					student.setGender(record.get("gender"));
					student.setStream(record.get("stream"));
					student.setUsername(record.get("username"));
					student.setContact(record.get("contact"));
					student.setSection(record.get("section"));
					student.setRollNo(record.get("roll_no"));
					student.setGuardianContact(record.get("guardian contact"));
					student.setDateOfBirth(df.parse(record.get("Date of birth")));
					student.setAdmissiondate(df.parse(record.get("Date of admission")));
					UserDto userDto = new UserDto();
					userDto.setFirstname(student.getFirstname());
					userDto.setLastname(student.getLastname());
					userDto.setRoles("student");
					userDto.setUsername(student.getUsername());
					userDto.setTenantId(orgId);
					userDto.setId(student.getId());
					boolean isCreated = keycloakUserManager.createUserInKeycloak(userDto);
					if (isCreated) {
						batch.add(student);
					}
					if (batch.size() == 100) {
						studentRepository.saveAll(batch);
						batch.clear();
					}
				}

				if (!batch.isEmpty()) {
					studentRepository.saveAll(batch);
					batch.clear();
				}

				LOG.debug("Job " + jobId + " finished.");
			} catch (Exception e) {
				LOG.error("Failed job with jobId: {} and error: {}", jobId, e);
			} finally {
				TenantContext.clear();
			}
		});
	}

	public Optional<Student> getStudentDetails(Long studentId) {
		return studentRepository.findById(studentId);
	}

	public AttendanceStatDto getStudentAttendanceGraph(Long studentId, LocalDate start, LocalDate end) {
		StudentAttendanceBitmap record = studentAttendanceRepo.findAttendanceByStudentId(studentId);

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

			// Count only attendance-taking days
			if (status != AttendanceStatus.NOT_MARKED && status != AttendanceStatus.HOLIDAY) {
				countableDays++;
				if (status == AttendanceStatus.PRESENT || status == AttendanceStatus.LATE) {
					presentCount++;
				}
			}

			// Monthly & Yearly grouping
			YearMonth ym = YearMonth.from(current);
			int year = current.getYear();

			monthMap.computeIfAbsent(ym, k -> new ArrayList<>()).add(status);
			yearMap.computeIfAbsent(year, k -> new ArrayList<>()).add(status);

			trend.add(new DailyAttendanceDto(current, status));
		}
		LocalDate lastMarked = trend.get(trend.size() - 1).getDate();
		int remainingDays = (int) ChronoUnit.DAYS.between(lastMarked, end);
		for (int i = 0; i < remainingDays; i++) {
			LocalDate current = lastMarked.plusDays(i + 1);
			trend.add(new DailyAttendanceDto(current, AttendanceStatus.NOT_MARKED));
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

	public Map<String, List<SubjectTermScoreDto>> getSubjectWiseMarks(Long studentId) {
		List<StudentSubjectPerformance> performances = studentPerformanceRepo.findByStudentId(studentId);

		return performances.stream().collect(Collectors.groupingBy(p -> p.getSubject().getName(), Collectors
				.mapping(p -> new SubjectTermScoreDto(p.getTerm(), p.getMarksObtained()), Collectors.toList())));
	}

	public Map<String, Float> getScoreDistribution(Long studentId) {
		StudentOverallPerformance perf = studentOverAllPerformanceRepo.findByStudentId(studentId).orElse(null);

		float academicScore = perf.getAverageScore() != null ? perf.getAverageScore() : 0;
		float extraScore = perf.getExtracurricularScore() != null ? perf.getExtracurricularScore() : 0;

		return Map.of("Academic", academicScore, "Extracurricular", extraScore);
	}

	public Student findById(Long studentId) {
		return studentRepository.findById(studentId).get();
	}

	public StudentAttendanceBitmap getStudentAttendance(Long id) {
		return studentAttendanceRepo.getAttendanceByStudentId(id);
	}

	@Transactional
	public StudentAttendanceBitmap saveStudentAttendance(Long studentId, AttendanceUpdateDto dto) {
		return saveAttendanceForStudent(studentId, dto.getTargetDate(), dto.getStaus(), dto.getDayIndex());
	}

	private StudentAttendanceBitmap saveAttendanceForStudent(Long studentId, LocalDate date, AttendanceStatus status,
			Integer dayIndex) {
		StudentAttendanceBitmap bitmapEntity = studentAttendanceRepo.getAttendanceByStudentId(studentId);
		if (bitmapEntity == null) {
			bitmapEntity = new StudentAttendanceBitmap();
			bitmapEntity.setAttendanceBitmap(new byte[1]);
			bitmapEntity.setCreatedBy(UserSessionContextHolder.getUsername());
			bitmapEntity.setStartDate(new Date());
			Student s = studentRepository.findById(studentId).get();
			bitmapEntity.setStudentId(s);
		}
		byte[] bitmap = bitmapEntity.getAttendanceBitmap();
		if (date == null) {
			bitmap = AttendanceUtils.updateAttendance(bitmap, dayIndex, status);
		} else {
			bitmap = AttendanceUtils.updateAttendance(bitmap,
					bitmapEntity.getStartDate().toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate(), date,
					status);
		}

		bitmapEntity.setAttendanceBitmap(bitmap);
		studentAttendanceRepo.save(bitmapEntity);
		return bitmapEntity;
	}

	public Student findByUsername(String userName) {
		return studentRepository.findByUsername(userName);
	}

	public Page<Student> getAllStudents(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getPageNumber(), searchDTO.getPageSize(),
				SortBuilder.buildSort(searchDTO));
		SpecificationBuilder<Student> specBuilder = new SpecificationBuilder<>();
		Specification<Student> spec = specBuilder.build(searchDTO.getFilters());
		return studentRepository.findAll(spec, pageable);
	}

	@Transactional
	public List<AttendanceDayDto> getAttendanceByClassAndSection(String className, String section, Date date) {
		List<Map<String, Object>> attendances = studentRepository.getAttendanceByClassnameAndSection(className,
				section);
		List<AttendanceDayDto> attendancedtos = new ArrayList<>();
		attendances.forEach(m -> {
			StudentAttendanceBitmap attendance = (StudentAttendanceBitmap) m.getOrDefault("attendance",
					new StudentAttendanceBitmap());
			AttendanceStatus status = null;
			if (attendance != null) {
				byte[] bitmap = attendance.getAttendanceBitmap();
				status = AttendanceUtils.readAttendance(bitmap,
						attendance.getStartDate().toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate(),
						date.toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate());
			}
			AttendanceDayDto a = new AttendanceDayDto();
			a.setStudentId(Long.valueOf(m.get("studentId").toString()));
			a.setFirstname(m.get("firstname").toString());
			a.setLastname(m.get("lastname").toString());
			a.setDay(section);
			a.setStatus(status);
			a.setRollNo(m.get("rollNo").toString());

			attendancedtos.add(a);
		});
		return attendancedtos;
	}

	@Transactional
	public void updateStudentAttendance(List<AttendanceDayDto> attendances, LocalDate date) {
		attendances.forEach(at -> {
			saveAttendanceForStudent(at.getStudentId(), date, at.getStatus(), null);
		});
	}

	public Map<String, List<Student>> getByClassNameAndSection(List<String> classes, List<String> sections) {
		return studentRepository.getByClassNameAndSection(classes, sections).stream()
				.collect(Collectors.groupingBy(s -> s.getClassName() + "-" + s.getSection()));
	}

	public Map<Long, Student> findByIds(List<Long> ids) {
		return studentRepository.findAllById(ids).stream()
				.collect(Collectors.toMap(Student::getId, Function.identity()));
	}

	public ResponseEntity<?> getAttendanceCount(LocalDate start) {
		List<StudentAttendanceBitmap> bitmapEntity = studentAttendanceRepo.findAll();
		Map<AttendanceStatus, Long> map = new HashMap<>();
		bitmapEntity.forEach(b -> {
			AttendanceStatus status = AttendanceUtils.readAttendance(b.getAttendanceBitmap(),
					b.getStartDate().toInstant().atZone(ZoneId.of("Asia/Kolkata")).toLocalDate(), start);
			map.put(status, map.getOrDefault(status, 0l) + 1);
		});
		return ResponseEntity.ok(map);
	}

	public Long getStudentCount() {
		return studentRepository.count();
	}

}
