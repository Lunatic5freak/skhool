package com.ba.skhool.iam.manager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.student.repository.OrganizationRepository;
import com.example.multitenant.dto.OrganizationDTO;
import com.example.multitenant.entity.Organization;

import jakarta.transaction.Transactional;

@Component
public class OrganizationManager {

	@Autowired
	private OrganizationRepository repo;

	@Autowired
	private UserManager userManager;

	@Transactional
	public Organization save(OrganizationDTO organizationDTO) {
		Organization org = new Organization();
		BeanUtils.copyProperties(organizationDTO, org);
		UserDto userDto = new UserDto();
		userDto.setUserName(organizationDTO.getAdministratorUsername());
		userDto.setRoles("admin");
		userDto.setTenantId(org.getId());
		userManager.saveUser(userDto);
		repo.save(org);
		return org;
	}

}
