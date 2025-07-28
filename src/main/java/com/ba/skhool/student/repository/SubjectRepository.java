package com.ba.skhool.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ba.skhool.student.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
