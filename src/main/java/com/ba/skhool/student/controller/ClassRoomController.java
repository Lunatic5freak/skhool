package com.ba.skhool.student.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.student.dto.ClassroomDTO;
import com.ba.skhool.student.dto.ResponseDTO;
import com.ba.skhool.student.entity.ClassRoom;
import com.ba.skhool.student.manager.ClassroomManager;

@RestController
@RequestMapping("/schdeule")
public class ClassRoomController {

	Logger LOG = LoggerFactory.getLogger(ClassRoomController.class);

	@Autowired
	private ClassroomManager classRoomManager;

	@PostMapping("/")
	public ResponseEntity<?> createClassSchedule(@RequestBody List<ClassroomDTO> classRoomDto) {
		try {
			classRoomManager.saveClassRooms(classRoomDto);
			return ResponseEntity.ok(ResponseDTO.builder().status(HttpStatus.SC_CREATED).success(true)
					.message("Saved Schedules").build());
		} catch (Exception e) {
			LOG.error("Error in saving classroom schedules");
			return ResponseEntity.ok(ResponseDTO.builder().status(HttpStatus.SC_INTERNAL_SERVER_ERROR).success(false)
					.message("Error in saving").build());
		}
	}

	@GetMapping("/")
	public ResponseEntity<List<ClassroomDTO>> getSchedulesByClassAndSection(@RequestParam String className,
			@RequestParam String section) {

		List<ClassRoom> schedules = classRoomManager.findByNameAndSectionAndIsDeletedFalse(className, section);

		List<ClassroomDTO> response = schedules.stream().map(schedule -> {
			ClassroomDTO res = new ClassroomDTO();
			res.setId(schedule.getId());
			res.setName(schedule.getName());
			res.setSection(schedule.getSection());
			res.setStartTime(schedule.getStartTime());
			res.setEndTime(schedule.getEndTime());
			res.setSubjectId(schedule.getSubject().getId());
			res.setSubjectName(schedule.getSubject().getName());
			return res;
		}).toList();

		return ResponseEntity.ok(response);
	}
}
