package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstname;
	private String lastname;
	private String username;
	private Long organization;

	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Column(name = "date_of_admission")
	private Date admissiondate;

	@Column(name = "class")
	private String className;
	private String section;
	private String stream;
	private String bio;

	@Column(name = "roll_no")
	private String rollNo;

	@Column(name = "profile_pic")
	private String profilePic;
	private String contact;

	@Column(name = "organization_email")
	private String organizationEmail;
	private String gender;
	private String guardian;

	@Column(name = "guardian_contact")
	private String guardianContact;

	@Column(name = "guardian_verified")
	private Boolean guardianVerified;

	@Column(name = "extra_curricular")
	private String extraCurricular;

	@Column(name = "address")
	private String address;

	@JoinColumn(name = "attendance_id")
	@OneToOne(fetch = FetchType.LAZY)
	private StudentAttendanceBitmap attendance;

	@Column(name = "created_date", nullable = false)
	private OffsetDateTime createdDate = OffsetDateTime.now();

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_date", nullable = false)
	private OffsetDateTime updatedDate = OffsetDateTime.now();

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

	@JsonIgnore
	public String getName() {
		return this.firstname.concat(" ").concat(this.lastname);
	}

}