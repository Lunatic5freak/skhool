package com.ba.skhool.student.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classroom")
@Data
@NoArgsConstructor
public class ClassRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String section;

	@ManyToMany(mappedBy = "assignedClasses")
	private Set<Teacher> teachers = new HashSet<>();

	@Column(name = "created_date", nullable = false)
	private OffsetDateTime createdDate = OffsetDateTime.now();

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_date", nullable = false)
	private OffsetDateTime updatedDate = OffsetDateTime.now();

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;

}
