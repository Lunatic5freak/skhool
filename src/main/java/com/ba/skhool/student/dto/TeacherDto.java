package com.ba.skhool.student.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Biswabijayee Mohanty
 *
 */
@Data
@NoArgsConstructor
public class TeacherDto {
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private Long organization;
	private String category;
	private String qualification;
	private String bio;
	private String profilePic;
	private String contact;
	private String organizationEmail;
	private String personalEmail;
	private String gender;
	private String role;
	private String idProof;
	private Date dateOfJoining;
	private Date dateOfBirth;

}
