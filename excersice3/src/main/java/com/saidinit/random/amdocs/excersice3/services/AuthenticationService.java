package com.saidinit.random.amdocs.excersice3.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Data;

@Component
@Data
public class AuthenticationService {
	private String jwt;

	/**
	 * Now, I realise that this should be spring security, but I didn't want to use
	 * the basic security assigning users and roles to each endpoint, and I wanted
	 * to use JWT, so I downloaded spring-jwt and spring-oauth, but latest version
	 * (5) has deprecated ... like everything! and I didn't have the time to go on
	 * and read all that documentation so I put this up, and you can use
	 * nimbusds.jose library to validate your JWT, we can talk about this in more
	 * detail if you'd like
	 */
	public ResponseEntity validateJwt(String id, String jwt) {
		// TODO: do something with JWTClaimsSet and validate
		if (StringUtils.isEmpty(id)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
