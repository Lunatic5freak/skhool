package com.ba.skhool.student.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

	private boolean success;
	private String message;
	private Integer status;

}
