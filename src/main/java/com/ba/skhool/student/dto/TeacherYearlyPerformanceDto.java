package com.ba.skhool.student.dto;

public record TeacherYearlyPerformanceDto(String year, Float averageResultScore, Float passRatePercentage,
		Float feedbackScore) {
}
