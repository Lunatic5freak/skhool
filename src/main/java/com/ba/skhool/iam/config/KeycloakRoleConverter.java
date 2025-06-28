package com.ba.skhool.iam.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public class KeycloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
		delegate.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
		return delegate.convert(jwt);
	}

	private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
		var roles = (Collection<String>) jwt.getClaim("roles");
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}
}
