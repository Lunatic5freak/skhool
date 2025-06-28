package com.ba.skhool.iam.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

	@Value("${keycloak.clientId}")
	private String clientId;

	@Value("${keycloak.clientSecret}")
	private String clientSecret;

	@Value("${keycloak.baseUrl}")
	private String baseUrl;

	@Value("${keycloak.relam}")
	private String relam;

	@Bean
	public Keycloak registerKeycloakAdmin() {
		return KeycloakBuilder.builder().serverUrl(baseUrl).realm(relam).clientId(clientId).clientSecret(clientSecret)
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS).build();
	}
}
