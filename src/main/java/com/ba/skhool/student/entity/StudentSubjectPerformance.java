package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Biswabijayee Mohanty
 *
 */
@Entity
@Table(name = "student_subject_performance")
public class StudentSubjectPerformance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	private String term;

	@Column(name = "marks_obtained")
	private Float marksObtained;

	@Column(name = "max_marks")
	private Float maxMarks;

	@Column(name = "performance_remark")
	private String performanceRemark;

	@Column(name = "exam_date")
	private Date examDate;

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

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Float getMarksObtained() {
		return marksObtained;
	}

	public void setMarksObtained(Float marksObtained) {
		this.marksObtained = marksObtained;
	}

	public Float getMaxMarks() {
		return maxMarks;
	}

	public void setMaxMarks(Float maxMarks) {
		this.maxMarks = maxMarks;
	}

	public String getPerformanceRemark() {
		return performanceRemark;
	}

	public void setPerformanceRemark(String performanceRemark) {
		this.performanceRemark = performanceRemark;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
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
