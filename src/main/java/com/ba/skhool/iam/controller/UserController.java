package com.ba.skhool.iam.controller;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.iam.entity.User;
import com.ba.skhool.iam.manager.UserManager;
import com.ba.skhool.student.dto.StudentDTO;
import com.ba.skhool.student.dto.TeacherDto;
import com.ba.skhool.student.entity.Student;
import com.ba.skhool.student.entity.Teacher;
import com.ba.skhool.student.manager.StudentManager;
import com.ba.skhool.student.manager.TeacherManager;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserManager userManager;

	@Autowired
	private StudentManager studentManager;

	@Autowired
	private TeacherManager teacherManager;

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId) {
		User user = userManager.getUserById(userId);
		if (user == null) {
			return ResponseEntity.ok(Map.of("status", "User Not found"));
		}
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.setUsername(user.getUsername());
		userDto.setRoles(user.getRole());
		userDto.setTenantId(UserSessionContextHolder.getTenantId());
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/me")
	public ResponseEntity<?> getUserDetails() {
		String userName = UserSessionContextHolder.getUsername();
		String role = null;
		try {
			role = UserSessionContextHolder.getRole().stream().toList().get(0).getAuthority();
		} catch (Exception e) {
			role = String.valueOf(UserSessionContextHolder.getRole().stream().toList().get(0));
		}
		if (role.contains("teacher")) {
			Teacher teacher = teacherManager.findByUsername(userName);
			TeacherDto teacherDto = new TeacherDto();
			teacherDto.setRole(role);
			BeanUtils.copyProperties(teacher, teacherDto);
			return ResponseEntity.ok(teacherDto);
		} else if (role.contains("student")) {
			Student student = studentManager.findByUsername(userName);
			if (student == null) {
				return ResponseEntity.ok(Map.of("status", "not found"));
			}
			StudentDTO studentDto = new StudentDTO();
			BeanUtils.copyProperties(student, studentDto);
			return ResponseEntity.ok(studentDto);
		}
		User user = userManager.findByUsername(userName);
		if (user == null) {
			return ResponseEntity.ok(Map.of("status", "User Not found"));
		}
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.setUsername(user.getUsername());
		userDto.setRoles(user.getRole());
		userDto.setTenantId(UserSessionContextHolder.getTenantId());
		return ResponseEntity.ok(userDto);
	}

}
