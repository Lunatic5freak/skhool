package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.TeacherSubjectMap;

@Repository
public interface TeacherSubjectMapRepository extends JpaRepository<TeacherSubjectMap, Long> {
	List<TeacherSubjectMap> findByTeacher_IdAndIsDeletedFalse(Long teacherId);

	List<TeacherSubjectMap> findBySubject_IdAndIsDeletedFalse(Long subjectId);
}
