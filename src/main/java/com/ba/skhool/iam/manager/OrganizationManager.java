package com.ba.skhool.iam.manager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.student.repository.OrganizationRepository;
import com.example.multitenant.cache.TenantMetadata;
import com.example.multitenant.context.TenantDataholder;
import com.example.multitenant.dto.OrganizationDTO;
import com.example.multitenant.entity.Organization;

import jakarta.transaction.Transactional;

@Component
public class OrganizationManager {

	@Autowired
	private OrganizationRepository repo;

	@Autowired
	private UserManager userManager;

	@Autowired
	private ApplicationContext appContext;

	@Transactional
	public Organization save(OrganizationDTO organizationDTO) {
		Organization org = new Organization();
		BeanUtils.copyProperties(organizationDTO, org);
		repo.save(org);
		UserDto userDto = new UserDto();
		userDto.setUsername(organizationDTO.getAdministratorUsername());
		userDto.setRoles("admin");
		userDto.setTenantId(org.getId());
		userManager.saveUser(userDto);
		TenantMetadata info = new TenantMetadata();
		info.setTenantId(org.getId());
		info.setSchema(org.getSchemaName());
		info.setName(org.getName());
		info.setCluster(org.getClusterAddress());
		info.setUsername(appContext.getEnvironment().getProperty("database.userName"));
		info.setPassword(appContext.getEnvironment().getProperty("database.password"));
		TenantDataholder.addTenant(info);
		return org;
	}

}
