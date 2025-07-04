package com.ba.skhool.student.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ba.skhool.student.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

	public List<Teacher> findTeacherByOrganization(Long organization);

	public Teacher findByUsername(String username);

	@Query(value = "select tc.teacher_id,c.name,c.section from teacher_classroom tc join classroom c on c.id=tc.classroom_id where tc.teacher_id=?1", nativeQuery = true)
	List<Map<String, Object>> findByIdWithClasses(Long id);

}
