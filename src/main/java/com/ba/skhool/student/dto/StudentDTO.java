package com.ba.skhool.student.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDTO {
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private Long organization;

	private String className;
	private String section;
	private String stream;
	private String bio;

	private String profilePic;
	private String contact;

	private String organizationEmail;
	private String gender;
	private String guardian;

	private String guardianContact;

	private Boolean guardianVerified;

	private String extraCurricular;

	private String address;

	private String rollNo;
}
