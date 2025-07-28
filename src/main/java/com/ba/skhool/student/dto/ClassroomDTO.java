package com.ba.skhool.student.dto;

import java.time.OffsetDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClassroomDTO {

	private Long id;
	private String name;
	private String section;
	private OffsetDateTime startTime;
	private OffsetDateTime endTime;
	private Long subjectId;
	private String subjectName;
	private String weekDay;
}
