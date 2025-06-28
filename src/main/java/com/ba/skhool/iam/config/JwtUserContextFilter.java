package com.ba.skhool.iam.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ba.skhool.iam.context.UserSessionContext;
import com.ba.skhool.iam.context.UserSessionContextHolder;
import com.example.multitenant.context.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtUserContextFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
			Jwt jwt = jwtAuthToken.getToken();
			String username = jwt.getClaimAsString("preferred_username");
			String tenantId = jwt.getClaimAsString("tenantId"); // custom claim if you have
			TenantContext.setTenantId(Long.valueOf(tenantId));
			// Set in your context holder
			UserSessionContext context = new UserSessionContext();
			context.setUsername(username);
			context.setRole(jwt.getClaim("roles"));
			context.setTenantId(Long.valueOf(tenantId));

			UserSessionContextHolder.setContext(context);
		}

		filterChain.doFilter(request, response);
	}
}
