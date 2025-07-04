package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.StudentSubjectPerformance;

@Repository
public interface GradeRepository extends JpaRepository<StudentSubjectPerformance, Long> {
	List<StudentSubjectPerformance> findByTermAndStudent_classNameAndStudent_SectionAndIsDeletedFalse(String term,
			String studentClass, String section);

	List<StudentSubjectPerformance> findByStudent_classNameAndStudent_SectionAndIsDeletedFalse(String studentClass,
			String section);

	List<StudentSubjectPerformance> findByStudent_classNameAndIsDeletedFalse(String studentClass);

	List<StudentSubjectPerformance> findByTermAndStudent_classNameAndIsDeletedFalse(String term, String studentClass);
}
