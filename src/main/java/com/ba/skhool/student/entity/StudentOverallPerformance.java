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
@Table(name = "student_overall_performance")
public class StudentOverallPerformance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@Column(name = "academic_year")
	private String academicYear;

	@Column(name = "attendance_percentage")
	private Float attendancePercentage;

	@Column(name = "overall_grade")
	private String overallGrade;

	@Column(name = "improvement_notes")
	private String improvementNotes;

	@Column(name = "extra_cucrricular_score")
	private Float extracurricularScore;

	@Column(name = "total_subjects")
	private Integer totalSubjects;

	@Column(name = "average_score")
	private Float averageScore;

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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public Float getAttendancePercentage() {
		return attendancePercentage;
	}

	public void setAttendancePercentage(Float attendancePercentage) {
		this.attendancePercentage = attendancePercentage;
	}

	public String getOverallGrade() {
		return overallGrade;
	}

	public void setOverallGrade(String overallGrade) {
		this.overallGrade = overallGrade;
	}

	public String getImprovementNotes() {
		return improvementNotes;
	}

	public void setImprovementNotes(String improvementNotes) {
		this.improvementNotes = improvementNotes;
	}

	public Float getExtracurricularScore() {
		return extracurricularScore;
	}

	public void setExtracurricularScore(Float extracurricularScore) {
		this.extracurricularScore = extracurricularScore;
	}

	public Integer getTotalSubjects() {
		return totalSubjects;
	}

	public void setTotalSubjects(Integer totalSubjects) {
		this.totalSubjects = totalSubjects;
	}

	public Float getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(Float averageScore) {
		this.averageScore = averageScore;
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