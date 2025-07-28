package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "class_subject", uniqueConstraints = { @UniqueConstraint(columnNames = { "class_id", "subject_id" }),
		@UniqueConstraint(columnNames = { "subject_id", "class_id" }) })
@Data
@NoArgsConstructor
public class ClassSubjectMap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//    @Column(name = "class_id", nullable = false)
//    private Integer classId;
//
//    @Column(name = "subject_id", nullable = false)
//    private Integer subjectId;

	// Foreign key relations (optional to map)
	@ManyToOne
	@JoinColumn(name = "class_id", insertable = false, updatable = false)
	private SchoolClass classId;

	@ManyToOne
	@JoinColumn(name = "subject_id", insertable = false, updatable = false)
	private Subject subject;

	@Column(name = "created_date", nullable = false)
	private OffsetDateTime createdDate = OffsetDateTime.now();

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_date", nullable = false)
	private OffsetDateTime updatedDate = OffsetDateTime.now();

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

}
