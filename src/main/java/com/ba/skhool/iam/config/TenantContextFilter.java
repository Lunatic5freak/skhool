package com.ba.skhool.iam.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ba.skhool.iam.context.UserSessionContext;
import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.example.multitenant.context.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TenantContextFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.isAuthenticated()) {
				OidcUser user = (OidcUser) authentication.getPrincipal();
				TenantContext.setTenantId(Long.valueOf(user.getClaimAsString("tenantId")));
				UserSessionContext context = new UserSessionContext();
				context.setRole(user.getAuthorities());
				context.setTenantId(Long.valueOf(user.getClaimAsString("tenantId")));
				context.setUserId(user.getClaim("userId"));
				context.setUsername(user.getPreferredUsername());
				context.setRole(user.getAuthorities());
				UserSessionContextHolder.setContext(context);
			}

			filterChain.doFilter(request, response);
		} finally {
			TenantContext.clear(); // Ensure cleanup
			UserSessionContextHolder.clear();

		}

	}

}
