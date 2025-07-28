package com.ba.skhool.student.manager;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.student.dto.ResponseDTO;
import com.ba.skhool.student.dto.TeacherSubjectMappingRequest;
import com.ba.skhool.student.entity.Subject;
import com.ba.skhool.student.entity.Teacher;
import com.ba.skhool.student.entity.TeacherSubjectMap;
import com.ba.skhool.student.repository.TeacherSubjectMapRepository;

import jakarta.transaction.Transactional;

@Component
public class TeacherSubjectMapManager {

	@Autowired
	private TeacherManager teacherManager;

	@Autowired
	private SubjectManager subjectManager;

	@Autowired
	private TeacherSubjectMapRepository mappingRepository;

	@Transactional
	public ResponseEntity<?> saveMappings(TeacherSubjectMappingRequest request) {
		List<Teacher> teachers = teacherManager.findAllById(request.getTeacherIds());
		List<Subject> subjects = subjectManager.findAllById(request.getSubjectIds());

		if (teachers.isEmpty() || subjects.isEmpty()) {
			return ResponseEntity.badRequest().body("Invalid teacher or subject IDs");
		}

		List<TeacherSubjectMap> mappings = new ArrayList<>();
		for (Teacher teacher : teachers) {
			for (Subject subject : subjects) {
				TeacherSubjectMap map = new TeacherSubjectMap();
				map.setTeacher(teacher);
				map.setSubject(subject);
				map.setCreatedBy(UserSessionContextHolder.getUsername());
				map.setCreatedDate(OffsetDateTime.now());
				map.setUpdatedDate(OffsetDateTime.now());
				map.setIsDeleted(false);
				mappings.add(map);
			}
		}
		mappingRepository.saveAll(mappings);
		return ResponseEntity.ok(ResponseDTO.builder().success(true).message("Mappings created").build());
	}
}
