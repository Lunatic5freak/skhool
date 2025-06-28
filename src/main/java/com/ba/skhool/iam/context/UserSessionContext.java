package com.ba.skhool.iam.context;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class UserSessionContext {
	private Long userId;
	private String username;
	private Collection<? extends GrantedAuthority> role;
	private Long tenantId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Collection<? extends GrantedAuthority> getRole() {
		return role;
	}

	public void setRole(Collection<? extends GrantedAuthority> collection) {
		this.role = collection;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return "UserSessionContext [userId=" + userId + ", username=" + username + ", role=" + role + ", tenantId="
				+ tenantId + "]";
	}

}