package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.TeacherPerformance;

@Repository
public interface TeacherPerformanceRepository extends JpaRepository<TeacherPerformance, Long> {

	@Query("SELECT p FROM TeacherPerformance p WHERE p.teacher.id = :teacherId")
	public List<TeacherPerformance> findAllByTeacherId(@Param("teacherId") Long teacherId);

	@Query("SELECT p FROM TeacherPerformance p WHERE p.teacher.id = :studentId AND p.academicYear= : year")
	public TeacherPerformance findByTeacherIdAndacademicYear(@Param("teacherId") Long teacherId,
			@Param("year") String year);

}
