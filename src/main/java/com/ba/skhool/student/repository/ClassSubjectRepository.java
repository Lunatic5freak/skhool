package com.ba.skhool.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.ClassSubjectMap;

@Repository
public interface ClassSubjectRepository extends JpaRepository<ClassSubjectMap, Long> {

	List<ClassSubjectMap> getByClassId_id(Long classId);

}
