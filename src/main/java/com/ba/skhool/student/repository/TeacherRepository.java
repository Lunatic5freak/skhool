package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ba.skhool.student.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

	public List<Teacher> findTeacherByOrganization(Long organization);

	public Teacher findByUsername(String username);

}
