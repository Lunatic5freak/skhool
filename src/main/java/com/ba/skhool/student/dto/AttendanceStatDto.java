package com.ba.skhool.student.dto;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceStatDto {
	private double overallPercentage;
	private Map<YearMonth, Double> monthlyPercentage;
	private Map<Integer, Double> yearlyPercentage;
	private List<DailyAttendanceDto> trend; // for graph plotting
}