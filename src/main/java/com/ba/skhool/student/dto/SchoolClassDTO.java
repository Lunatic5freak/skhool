package com.ba.skhool.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClassDTO {

	private Long id;

	private Long organization;

	private String className;
	private Integer sections;

}
