package com.ba.skhool.student.dto;

import lombok.Data;

@Data
public class UpdateTeacherPerformanceDto {
	private Float averageResultScore;
	private Float passRatePercentage;
	private Float teacherFeedbackScore;
	private String subjectIds;
	private String remarks;
}
