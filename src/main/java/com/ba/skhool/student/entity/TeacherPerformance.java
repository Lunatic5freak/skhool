package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * @author Biswabijayee Mohanty
 *
 */
@Entity
@Table(name = "teacher_performance")
public class TeacherPerformance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;

	@Column(name = "academic_year")
	private String academicYear;

	@Column(name = "average_result_score")
	private Float averageResultScore;

	@Column(name = "pass_rate_percentage")
	private Float passRatePercentage;

	@Column(name = "teacher_feedback_score")
	private Float teacherFeedbackScore;

	@Column(name = "subject_ids")
	private String subjectIds;
	private String remarks;

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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public Float getAverageResultScore() {
		return averageResultScore;
	}

	public void setAverageResultScore(Float averageResultScore) {
		this.averageResultScore = averageResultScore;
	}

	public Float getPassRatePercentage() {
		return passRatePercentage;
	}

	public void setPassRatePercentage(Float passRatePercentage) {
		this.passRatePercentage = passRatePercentage;
	}

	public Float getTeacherFeedbackScore() {
		return teacherFeedbackScore;
	}

	public void setTeacherFeedbackScore(Float teacherFeedbackScore) {
		this.teacherFeedbackScore = teacherFeedbackScore;
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
}
