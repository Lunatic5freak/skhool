package com.ba.skhool.iam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class KeycloakConfig {

	@Value("${keycloak.clientId}")
	private String clientId;

	@Value("${keycloak.clientSecret}")
	private String clientSecret;

	@Value("${keycloak.relam}")
	private String relam;

	@Value("${keycloak.baseUrl}")
	private String keycloakUrl;

//	@Value("${keycloak.authUri}")
//	private String authUri;
//
//	@Value("${keycloak.tokenUri}")
//	private String tokenUri;
//
//	@Value("${keycloak.userInfoUri}")
//	private String userInfoUri;
//
//	@Value("${keycloak.jwkUri}")
//	private String jwkUri;
//
//	@Value("${keycloak.issuerUri}")
//	private String issuerUri;

//	keycloak.userInfoUri=https://keycloak-21.onrender.com/realms/spring-app/protocol/openid-connect/userinfo
//		keycloak.tokenUri=https://keycloak-21.onrender.com/realms/spring-app/protocol/openid-connect/token
//			keycloak.jwkUri=https://keycloak-21.onrender.com/realms/spring-app/protocol/openid-connect/certs
//				keycloak.issuerUri=https://keycloak-21.onrender.com/realms/spring-app
//					keycloak.authUri=https://keycloak-21.onrender.com/realms/spring-app/protocol/openid-connect/auth

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(keycloakClientRegistration());
	}

	private ClientRegistration keycloakClientRegistration() {
		return ClientRegistration.withRegistrationId("keycloak").clientId(clientId).clientSecret(clientSecret)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("{baseUrl}/login/oauth2/code/keycloak").scope("openid", "profile", "email")
				.authorizationUri(String.format("%s/realms/%s/protocol/openid-connect/auth", keycloakUrl, relam))
				.tokenUri(String.format("%s/realms/%s/protocol/openid-connect/token", keycloakUrl, relam))
				.userInfoUri(String.format("%s/realms/%s/protocol/openid-connect/userinfo", keycloakUrl, relam))
				.userNameAttributeName("preferred_username")
				.jwkSetUri(String.format("%s/realms/%s/protocol/openid-connect/certs", keycloakUrl, relam))
				.issuerUri(String.format("%s/realms/%s", keycloakUrl, relam)).build();
	}
}
