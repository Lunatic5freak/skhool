package com.ba.skhool.iam.context;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Biswabijayee Mohanty
 *
 */
public class UserSessionContextHolder {
	private static final ThreadLocal<UserSessionContext> contextHolder = new ThreadLocal<>();

	public static void setContext(UserSessionContext context) {
		contextHolder.set(context);
	}

	public static UserSessionContext getContext() {
		return contextHolder.get();
	}

	public static void clear() {
		contextHolder.remove();
	}

	public static Long getUserId() {
		return getContext().getUserId();
	}

	public static String getUsername() {
		return getContext().getUsername();
	}

	public static Collection<? extends GrantedAuthority> getRole() {
		return getContext().getRole();
	}

	public static Long getTenantId() {
		return getContext().getTenantId();
	}
}