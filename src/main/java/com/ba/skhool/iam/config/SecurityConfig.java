package com.ba.skhool.iam.config;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SecurityProperties restSecProps;

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepo;

	@Bean
	public AuthenticationEntryPoint restAuthenticationEntryPoint() {
		return (httpServletRequest, httpServletResponse, e) -> {
			Map<String, Object> errorObject = new HashMap<>();
			int errorCode = 401;
			errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
			errorObject.put("error", HttpStatus.UNAUTHORIZED);
			errorObject.put("code", errorCode);
			errorObject.put("timestamp", new Timestamp(new Date().getTime()));
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			httpServletResponse.setStatus(errorCode);
			httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));
		};
	}

	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(c -> c.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
				.cors().configurationSource(corsConfigurationSource()).and()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/actuator/**", "/public/**", "/auth/login", "/teacher/**", "/swagger-ui/**",
								"/v3/**")
						.permitAll().requestMatchers("/admin/**").hasRole("admin").requestMatchers("/teacher/**")
						.hasAnyRole("teacher", "admin").requestMatchers("/student/**", "/grades/**")
						.hasAnyRole("student", "teacher", "admin").requestMatchers("/user/**", "grades/{id}/student")
						.hasAnyRole("admin", "teacher", "student", "master", "HOS-1", "HOS-2").anyRequest()
						.authenticated())
				.oauth2Login(auth -> auth.successHandler(authenticationSuccessHandler())
						.userInfoEndpoint(c -> c.oidcUserService(oidcUserService())))
				.oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakRoleConverter())))
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl(
						"http://localhost:8080/realms/spring-app/protocol/openid-connect/logout?post_logout_redirect_uri=http://localhost:8082/")
						.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID").permitAll())
				.addFilterAfter(new JwtUserContextFilter(), BearerTokenAuthenticationFilter.class)
				.addFilterAfter(new TenantContextFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) -> {
			HttpSession session = request.getSession(false);
			String redirect = "/";

			if (session != null && session.getAttribute("REDIRECT_URI") != null) {
				redirect = session.getAttribute("REDIRECT_URI").toString();
				session.setAttribute("user", "");
				session.removeAttribute("REDIRECT_URI");
			}
			List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
			String rolesJson = new ObjectMapper().writeValueAsString(roles);
			String encodedRoles = Base64.getEncoder().encodeToString(rolesJson.getBytes(StandardCharsets.UTF_8));
			// Redirect back to frontend (custom domain if needed)
			Cookie cookie = new Cookie("USER", encodedRoles);
			String serverName = request.getServerName(); // e.g., "localhost" or "frontend.example.com"
			cookie.setDomain(serverName);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			response.sendRedirect(redirect);
		};
	}

	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		return userRequest -> {
			OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			List<String> roles = (List<String>) oidcUser.getClaims().get("roles");
			for (String role : roles) {
				mappedAuthorities.add(new SimpleGrantedAuthority(role));
			}

			return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost") // or http://localhost:3000 for local
						.allowedMethods("*").allowCredentials(true); // <== Important
			}
		};
	}

}
