package com.ba.skhool.student.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

	List<Student> findByclassNameAndSection(String className, String section);

	public Student findByUsername(String userName);

	Page<Student> findAll(Specification<Student> spec, Pageable pageable);

	@Query("Select s.id as studentId, s.firstname as firstname, s.lastname as lastname, s.rollNo as rollNo, sb as attendance from Student s left join StudentAttendanceBitmap sb on s.id=sb.studentId.id where className= :className and section= :section")
	List<Map<String, Object>> getAttendanceByClassnameAndSection(String className, String section);

}
