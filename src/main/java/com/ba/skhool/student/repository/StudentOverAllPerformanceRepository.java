package com.ba.skhool.student.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.StudentOverallPerformance;

@Repository
public interface StudentOverAllPerformanceRepository extends JpaRepository<StudentOverallPerformance, Long> {

	@Query("SELECT p FROM StudentOverallPerformance p WHERE p.student.id = :studentId")
	Optional<StudentOverallPerformance> findByStudentId(@Param("studentId") Long studentId);

}
