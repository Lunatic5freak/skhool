package com.ba.skhool.student.dto;

import java.util.Date;

import lombok.Data;

@Data
public class StudentGradeInputDTO {
	private Long studentId;
	private Long subjectId;
	private String term;
	private Float marksObtained;
	private Float maxMarks;
	private String performanceRemark;
	private Date examDate;
	private String createdBy;
}
