package com.ba.skhool.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.multitenant.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
