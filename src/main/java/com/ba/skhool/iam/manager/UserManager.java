package com.ba.skhool.iam.manager;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.ba.skhool.iam.dto.UserDto;
import com.ba.skhool.iam.entity.User;
import com.ba.skhool.iam.repository.UserRepository;
import com.ba.skhool.student.manager.KeycloakUserManager;

import jakarta.transaction.Transactional;

@Component
public class UserManager {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private KeycloakUserManager keycloakManager;

	@Transactional
	public User saveUser(UserDto userDto) {
		User dbUser = new User();
		BeanUtils.copyProperties(userDto, dbUser);
		dbUser.setUsername(userDto.getUsername());
		dbUser.setOrganizationId(
				userDto.getTenantId() != null ? userDto.getTenantId() : UserSessionContextHolder.getTenantId());
		dbUser.setCreatedBy(UserSessionContextHolder.getUsername());
		dbUser.setCreatedDate(OffsetDateTime.now());
		dbUser.setUpdatedDate(OffsetDateTime.now());
		boolean isCreated = keycloakManager.createUserInKeycloak(userDto);
		if (isCreated) {
			userRepo.save(dbUser);
			return dbUser;
		}
		return null;
	}

	public User getUserById(Long userId) {
		return Optional.of(userRepo.findById(userId)).get().orElse(null);
	}

	public User findByUsername(String userName) {
		return userRepo.findByUsername(userName);
	}

}
