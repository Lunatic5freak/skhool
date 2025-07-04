package com.ba.skhool.student.manager;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.student.dto.ResponseDTO;
import com.ba.skhool.student.dto.StudentGradeInputDTO;
import com.ba.skhool.student.dto.StudentGradeSubmissionRequestDTO;
import com.ba.skhool.student.entity.Student;
import com.ba.skhool.student.entity.StudentSubjectPerformance;
import com.ba.skhool.student.entity.Subject;
import com.ba.skhool.student.repository.GradeRepository;

import jakarta.transaction.Transactional;

@Component
public class GradeManager {

	private static final Logger LOG = LoggerFactory.getLogger(GradeManager.class);

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private SubjectManager subjectManager;

	@Autowired
	private GradeRepository gradeRepo;

	@Transactional
	public ResponseDTO saveAll(StudentGradeSubmissionRequestDTO request) {
		try {
			List<StudentSubjectPerformance> performances = new ArrayList<>();
			Map<Long, Student> students = studentManager
					.findByIds(request.getGrades().stream().map(s -> s.getStudentId()).toList());
			Map<Long, Subject> subjects = subjectManager
					.findByIds(request.getGrades().stream().map(StudentGradeInputDTO::getSubjectId).toList());

			for (StudentGradeInputDTO dto : request.getGrades()) {

				StudentSubjectPerformance performance = new StudentSubjectPerformance();
				performance.setStudent(students.get(dto.getStudentId()));
				performance.setSubject(subjects.get(dto.getSubjectId()));
				performance.setTerm(dto.getTerm());
				performance.setMarksObtained(dto.getMarksObtained());
				performance.setMaxMarks(dto.getMaxMarks());
				performance.setPerformanceRemark(dto.getPerformanceRemark());
				performance.setExamDate(dto.getExamDate());
				performance.setCreatedDate(OffsetDateTime.now());
				performance.setUpdatedDate(OffsetDateTime.now());
				performance.setIsDeleted(false);
				performance.setCreatedBy(UserSessionContextHolder.getUsername());

				performances.add(performance);
			}
			gradeRepo.saveAllAndFlush(performances);
			return ResponseDTO.builder().status(HttpStatus.SC_CREATED).success(true)
					.message("Grades saved successfully").build();
		} catch (Exception e) {
			LOG.error("Error while saving grades: {}", e);
			return ResponseDTO.builder().status(HttpStatus.SC_INTERNAL_SERVER_ERROR).success(false)
					.message(e.getMessage()).build();
		}
	}

	public List<StudentSubjectPerformance> findByStudentClassAndSection(String term, String studentClass,
			String section) {
		if (term == null && section != null) {
			return gradeRepo.findByStudent_classNameAndStudent_SectionAndIsDeletedFalse(studentClass, section);
		}
		if (term == null && section == null) {
			return gradeRepo.findByStudent_classNameAndIsDeletedFalse(studentClass);
		}
		if (section == null && term != null) {
			return gradeRepo.findByTermAndStudent_classNameAndIsDeletedFalse(term, studentClass);
		}
		return gradeRepo.findByTermAndStudent_classNameAndStudent_SectionAndIsDeletedFalse(term, studentClass, section);
	}

}
