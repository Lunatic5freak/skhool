package com.ba.skhool.student.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentGradeSubmissionRequestDTO {
	private String studentClass;
	private String section;
	private List<StudentGradeInputDTO> grades;
}
