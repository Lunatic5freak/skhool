package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.StudentAttendanceBitmap;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendanceBitmap, Long> {

	@Query("SELECT a FROM StudentAttendanceBitmap a WHERE a.studentId.id = :studentId")
	StudentAttendanceBitmap findAttendanceByStudentId(@Param("studentId") Long studentId);

	@Query("SELECT a FROM StudentAttendanceBitmap a WHERE a.studentId.id = :studentId")
	StudentAttendanceBitmap getAttendanceByStudentId(@Param("studentId") Long studentId);

	@Query("Select s from StudentAttendanceBitmap s where s.studentId.className= :className and s.studentId.section= :section")
	List<StudentAttendanceBitmap> getAttendanceByClassnameAndSection(String className, String section);

}
