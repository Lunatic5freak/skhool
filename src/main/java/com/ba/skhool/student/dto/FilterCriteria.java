package com.ba.skhool.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterCriteria {
	private String field; // e.g., "name"
	private String operator; // e.g., "EQUALS", "LIKE", "IN"
	private Object value; // e.g., "John", or List<String>
	private String condition; // e.g., "AND" or "OR" (default AND)

	// Getters and setters
}
