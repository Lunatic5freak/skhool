package com.ba.skhool.iam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ba.skhool.iam.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByOrganizationId(Long organization);

	public User findByUsername(String username);

}
