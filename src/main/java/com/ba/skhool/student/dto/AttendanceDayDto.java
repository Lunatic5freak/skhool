package com.ba.skhool.student.dto;

import com.ba.skhool.constants.AttendanceStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceDayDto {

	String firstname;
	String lastname;
	String rollNo;
	String day;
	AttendanceStatus status;
	Long studentId;

	public AttendanceDayDto(String day, AttendanceStatus status) {
		this.day = day;
		this.status = status;
	}
}
