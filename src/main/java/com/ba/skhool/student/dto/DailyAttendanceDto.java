package com.ba.skhool.student.dto;

import java.time.LocalDate;

import com.ba.skhool.constants.AttendanceStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyAttendanceDto {
	private LocalDate date;
	private AttendanceStatus status;

	public DailyAttendanceDto(LocalDate date, AttendanceStatus status) {
		this.date = date;
		this.status = status;
	}
}
