package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.StudentSubjectPerformance;

@Repository
public interface StudentPerformanceRepository extends JpaRepository<StudentSubjectPerformance, Long> {

	@Query("SELECT p FROM StudentSubjectPerformance p WHERE p.student.id = :studentId")
	List<StudentSubjectPerformance> findByStudentId(@Param("studentId") Long studentId);

}
