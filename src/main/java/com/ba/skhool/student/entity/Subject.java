package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Biswabijayee Mohanty
 *
 */
@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String code;
	private String description;
	private Long organization;
	@Column(name = "total_marks")
	private Double totalMarks;

	@Column(name = "created_date", nullable = false)
	private OffsetDateTime createdDate = OffsetDateTime.now();

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_date", nullable = false)
	private OffsetDateTime updatedDate = OffsetDateTime.now();

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;
}