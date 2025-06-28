package com.ba.skhool.iam.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Value("${keycloak.baseUrl}")
	private String keyclockUrl;

	@Value("${keycloak.relam}")
	private String relam;

	@Value("${redirect.uri}")
	private String redirectUri;

	@GetMapping("/login")
	public void initLogin(@RequestParam(defaultValue = "/") String redirect, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		session.setAttribute("REDIRECT_URI", redirect);

		response.sendRedirect("/api/oauth2/authorization/keycloak");
	}

	@GetMapping("/logout")
	public void logout(@AuthenticationPrincipal OidcUser oidcUser, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		try {
			String idToken = oidcUser.getIdToken().getTokenValue();
			String logoutUrl = keyclockUrl + "/realms/" + relam + "/protocol/openid-connect/logout" + "?id_token_hint="
					+ idToken + "&post_logout_redirect_uri=" + redirectUri;

			request.logout(); // Spring logout
			response.sendRedirect(logoutUrl); // Redirect to Keycloak logout
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/session")
	public ResponseEntity<?> getSession(HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		Cookie[] cookies = request.getCookies();
		String authHeader = request.getHeader("Authorization");
		Map<String, Object> map = new HashMap<>();
		map.put("cookies", cookies);
		map.put("auth", authHeader);
		map.put("sessionId", sessionId);
		return ResponseEntity.ok(map);
	}
}
