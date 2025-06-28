package com.ba.skhool.student.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.student.dto.TeacherDto;
import com.ba.skhool.student.entity.Teacher;

import jakarta.transaction.Transactional;

@Component
public class OrgAdminManager {

	@Autowired
	private TeacherManager teacherManager;

	@Transactional
	public Teacher saveTeacher(TeacherDto teacherDto) {
		Teacher teacher = teacherManager.save(teacherDto);
		return teacher;
	}

}
