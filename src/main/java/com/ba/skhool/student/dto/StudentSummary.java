package com.ba.skhool.student.dto;

import java.util.HashMap;
import java.util.Map;

import com.ba.skhool.student.entity.Student;

import lombok.Data;

@Data
public class StudentSummary {
	private Long studentId;
	private String studentName;
	private String classname;
	private String section;
	private String rollNo;
	private Map<String, Double> subjectMarks = new HashMap<>();
	private double overallPercentage;
	private double totalObtainedMarks;
	private double overAllMarks;
	private String remark;
	private String term;

	public StudentSummary(Student s, String remark, String term) {
		this.studentId = s.getId();
		this.studentName = s.getName();
		this.classname = s.getClassName();
		this.section = s.getSection();
		this.rollNo = s.getRollNo();
		this.remark = remark;
		this.term = term;
	}

	public void addSubjectMarks(String subject, double marks, double subjectTotalmarks) {
		subjectMarks.put(subject, marks);
		totalObtainedMarks = subjectMarks.values().stream().mapToDouble(Double::doubleValue).sum();
		this.overAllMarks += subjectTotalmarks;
		this.overallPercentage = (this.totalObtainedMarks / this.overAllMarks) * 100;
	}
}
