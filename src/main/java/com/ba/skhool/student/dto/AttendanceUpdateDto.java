package com.ba.skhool.student.dto;

import java.time.LocalDate;

import com.ba.skhool.constants.AttendanceStatus;

import lombok.Data;

@Data
public class AttendanceUpdateDto {
	private int dayIndex; // 0-based day index
	private LocalDate targetDate;
	private AttendanceStatus staus;
}
