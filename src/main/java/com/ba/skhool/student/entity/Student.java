package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;

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

@Entity
@Table(name = "students")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstname;
	private String lastname;
	private String username;
	private Long organization;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getOrganization() {
		return organization;
	}

	public void setOrganization(Long organization) {
		this.organization = organization;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOrganizationEmail() {
		return organizationEmail;
	}

	public void setOrganizationEmail(String organizationEmail) {
		this.organizationEmail = organizationEmail;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGuardian() {
		return guardian;
	}

	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}

	public String getGuardianContact() {
		return guardianContact;
	}

	public void setGuardianContact(String guardianContact) {
		this.guardianContact = guardianContact;
	}

	public Boolean getGuardianVerified() {
		return guardianVerified;
	}

	public void setGuardianVerified(Boolean guardianVerified) {
		this.guardianVerified = guardianVerified;
	}

	public String getExtraCurricular() {
		return extraCurricular;
	}

	public void setExtraCurricular(String extraCurricular) {
		this.extraCurricular = extraCurricular;
	}

	public OffsetDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(OffsetDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public OffsetDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(OffsetDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public StudentAttendanceBitmap getAttendance() {
		return attendance;
	}

	public void setAttendance(StudentAttendanceBitmap attendance) {
		this.attendance = attendance;
	}

	@JsonIgnore
	public String getName() {
		return this.firstname.concat(" ").concat(this.lastname);
	}

}