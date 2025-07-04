package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Biswabijayee Mohanty
 *
 */
@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
public class Teacher {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstname;
	private String lastname;
	private String username;
	private Long organization;
	private String category;
	private String qualification;
	private String bio;

	@Column(name = "profile_pic")
	private String profilePic;
	private String contact;

	@Column(name = "organization_email")
	private String organizationEmail;

	@JoinColumn(name = "attendance_id")
	@OneToOne(fetch = FetchType.LAZY)
	private TeachersAttendanceBitMap attendance;

	@Column(name = "personal_email")
	private String personalEmail;
	private String gender;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "teacher_classroom", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "classroom_id"))
	private Set<ClassRoom> assignedClasses;

	@Column(name = "created_date", nullable = false)
	private OffsetDateTime createdDate = OffsetDateTime.now();

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_date", nullable = false)
	private OffsetDateTime updatedDate = OffsetDateTime.now();

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

}