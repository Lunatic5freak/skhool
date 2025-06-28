package com.ba.skhool.iam.controller;

import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicRoutes {

	@Autowired
	private Keycloak keycloakAdmin;

	@GetMapping("/")
	public ResponseEntity<?> getReponse() {
		return ResponseEntity.ok(Map.of("msg", "hello"));
	}
}
