package com.ba.skhool.student.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeacherSubjectMappingRequest {
	private List<Long> teacherIds; // One or more teacher IDs
	private List<Long> subjectIds; // One or more subject IDs
}
