package com.ba.skhool.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ba.skhool.iam.manager.OrganizationManager;
import com.example.multitenant.dto.OrganizationDTO;
import com.example.multitenant.entity.Organization;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
	Logger logger = LoggerFactory.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationManager manager;

	@PostMapping("/")
	public ResponseEntity<?> createOrganization(@RequestBody OrganizationDTO organizationDto) {
		logger.debug("Inside create organization");
		Organization org = manager.save(organizationDto);
		return ResponseEntity.ok(org);
	}

}
