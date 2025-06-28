package com.ba.skhool.iam.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final OAuth2AuthorizationRequestResolver defaultResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String baseUri) {
		this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, baseUri);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
		return customize(req, request);
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
		return customize(req, request);
	}

	private OAuth2AuthorizationRequest customize(OAuth2AuthorizationRequest req, HttpServletRequest request) {
		if (req == null)
			return null;

		String redirectUri = request.getParameter("redirect_uri");
		System.out.println(redirectUri);
		Map<String, Object> additionalParams = new HashMap<>(req.getAdditionalParameters());
		if (redirectUri != null) {
			additionalParams.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
		}

		return OAuth2AuthorizationRequest.from(req).additionalParameters(additionalParams).build();
	}
}
