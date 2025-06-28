package com.ba.skhool.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.TeachersAttendanceBitMap;

@Repository
public interface TeacherAttendanceRepository extends JpaRepository<TeachersAttendanceBitMap, Long> {

	@Query("SELECT a.attendance FROM Teacher a WHERE a.id = :teacherId")
	TeachersAttendanceBitMap findAttendanceByTeacherId(@Param("teacherId") Long teacherId);

}
