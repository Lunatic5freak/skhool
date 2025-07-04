package com.ba.skhool.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectRequest {
	private Long id;

	private String name;
	private String code;
	private String description;
	private Double totalMarks;
}
