package com.ba.skhool.student.dto;

import java.time.LocalTime;

public record ScheduleDto(Long id, String day, String subject, String className, LocalTime startTime,
		LocalTime endTime) {
}
