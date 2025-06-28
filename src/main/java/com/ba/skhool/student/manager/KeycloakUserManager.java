package com.ba.skhool.student.manager;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ba.skhool.iam.dto.UserDto;

@Component
public class KeycloakUserManager {

	Logger LOG = LoggerFactory.getLogger(KeycloakUserManager.class);

	@Value("${keycloak.clientId}")
	private String clientId;

	@Value("${keycloak.relam}")
	private String realm;

	@Autowired
	private Keycloak keycloakAdmin;

	public boolean createUserInKeycloak(UserDto userDto) {
		try {
			LOG.info("Inside createUserInKeycloak");
			UserRepresentation user = new UserRepresentation();
			user.setUsername(userDto.getUserName());
			user.setEnabled(true);
			user.setEmail(userDto.getUserName());
			user.setEmailVerified(false);
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setClientRoles(Map.of(clientId, List.of(userDto.getRoles())));
			user.setAttributes(Map.of("tenantId", List.of(String.valueOf(userDto.getTenantId())), "role",
					List.of(userDto.getRoles())));
			CredentialRepresentation credential = new CredentialRepresentation();
			credential.setType(CredentialRepresentation.PASSWORD);
			credential.setValue("newpassword");
			credential.setTemporary(true);
			user.setCredentials(List.of(credential));
			Response res = keycloakAdmin.realm("spring-app").users().create(user);
			LOG.info("response from keycloak: {}", res);
			String userId = CreatedResponseUtil.getCreatedId(res);
			ClientRepresentation client = keycloakAdmin.realm(realm).clients().findByClientId(clientId).get(0);
			String clientUUID = client.getId();
			List<RoleRepresentation> clientRoles = keycloakAdmin.realm(realm).clients().get(clientUUID).roles().list();
			RoleRepresentation targetRole = clientRoles.stream()
					.filter(role -> role.getName().equals(userDto.getRoles())) // your
					.findFirst().orElseThrow();
			keycloakAdmin.realm(realm).users().get(userId).roles().clientLevel(clientUUID)
					.add(Collections.singletonList(targetRole));
			if (res.getStatus() == 201) {
				return true;
			}
			return false;

		} catch (Exception e) {
			LOG.error("Error occuered in createUserInKeycloak: {}", e);
			return false;
		}
	}

}
