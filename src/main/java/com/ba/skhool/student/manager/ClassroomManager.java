package com.ba.skhool.student.manager;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.student.dto.ClassroomDTO;
import com.ba.skhool.student.entity.ClassRoom;
import com.ba.skhool.student.entity.Subject;
import com.ba.skhool.student.repository.ClassRoomRepository;

import jakarta.transaction.Transactional;

@Component
public class ClassroomManager {

	@Autowired
	private ClassRoomRepository classRoomRepo;
	@Autowired
	private SubjectManager subjectManager;

	@Transactional
	public List<ClassRoom> saveClassRooms(List<ClassroomDTO> classRoomDtos) {
		Map<Long, Subject> subjects = subjectManager
				.findByIds(classRoomDtos.stream().map(c -> c.getSubjectId()).toList());
		List<ClassRoom> classrooms = new ArrayList<>();
		classRoomDtos.forEach(classroom -> {
			ClassRoom schedule = new ClassRoom();
			schedule.setName(classroom.getName());
			schedule.setSection(classroom.getSection());
			schedule.setStartTime(classroom.getStartTime());
			schedule.setEndTime(classroom.getEndTime());
			schedule.setSubject(subjects.get(classroom.getSubjectId()));
			schedule.setCreatedBy(UserSessionContextHolder.getUsername());
			schedule.setUpdatedDate(OffsetDateTime.now());
			schedule.setWeekDay(classroom.getWeekDay());
			classrooms.add(schedule);
		});
		classRoomRepo.saveAllAndFlush(classrooms);
		return classrooms;
	}

	public List<ClassRoom> findByNameAndSectionAndIsDeletedFalse(String className, String section) {
		return classRoomRepo.findByNameAndSectionAndIsDeletedFalse(className, section);
	}

}
