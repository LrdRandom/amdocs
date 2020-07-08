package com.saidinit.random.amdocs.excersice3.controllers;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saidinit.random.amdocs.excersice3.exceptions.UserForbiddenException;
import com.saidinit.random.amdocs.excersice3.exceptions.UserInternalServerException;
import com.saidinit.random.amdocs.excersice3.exceptions.UserNotFoundException;
import com.saidinit.random.amdocs.excersice3.exceptions.UserUnauthorisedException;
import com.saidinit.random.amdocs.excersice3.models.UserProfile;
import com.saidinit.random.amdocs.excersice3.services.AuthenticationService;
import com.saidinit.random.amdocs.excersice3.services.Excersice3Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@Data
@Validated
@RequestMapping("amdocs")
@Slf4j
public class Excersice3Controller {

	private static final String JSON_START = "{\"error\": \"";
	private static final String JSON_END = "\"}";
	private final Excersice3Service service;
	private final AuthenticationService authService;

	/**
	 * POST endpoint to update user profile picture
	 * 
	 * @param id   : the user id
	 * @param file : the picture file to upload
	 * @param auth : jwt token in the authorization header
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/user/{id}/profile/picture")
	public ResponseEntity updateUserProfilePicture(@PathVariable @Pattern(regexp = "[0-9]{9}") String id,
			@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String auth) {
		if (file.isEmpty()) {
			// TODO: throw some error 400: file is needed
		}

		ResponseEntity authResp = authService.validateJwt(id, auth);
		if (!authResp.getStatusCode().is2xxSuccessful()) {
			log.debug("Unauthorized attempt at the system");
			return authResp;
		}

		return new ResponseEntity<UserProfile>(service.updateUserProfilePicture(id, file), HttpStatus.ACCEPTED);
	}

	/**
	 * PUT endpoint to add internet service of a type to a user
	 * 
	 * @param id   : the user id
	 * @param type : the type of internet service
	 * @param auth : jwt token in the authorization header
	 */
	@SuppressWarnings("rawtypes")
	@PutMapping(path = "/user/{id}/internet-service/")
	public ResponseEntity requestInternetAccess(@PathVariable @Pattern(regexp = "[0-9]{9}") String id,
			@RequestParam("type") String type, @RequestHeader("Authorization") String auth) {
		ResponseEntity authResp = authService.validateJwt(id, auth);
		if (!authResp.getStatusCode().is2xxSuccessful()) {
			log.debug("Unauthorized attempt at the system");
			return authResp;
		}

		return new ResponseEntity<UserProfile>(service.requestInternetAccess(id, type), HttpStatus.CREATED);
	}

	/**
	 * DELETE endpoint to remove a subscription from a user
	 * 
	 * @param id   : the user id
	 * @param type : the type of internet service
	 * @param auth : jwt token in the authorization header
	 */
	@SuppressWarnings("rawtypes")
	@DeleteMapping(path = "/user/{id}/internet-service/")
	public ResponseEntity cancelSubscription(@PathVariable @Pattern(regexp = "[0-9]{9}") String id,
			@RequestParam("type") String type, @RequestHeader("Authorization") String auth) {
		ResponseEntity authResp = authService.validateJwt(id, auth);
		if (!authResp.getStatusCode().is2xxSuccessful()) {
			log.debug("Unauthorized attempt at the system");
			return authResp;
		}

		return new ResponseEntity<UserProfile>(service.cancelSubscription(id, type), HttpStatus.ACCEPTED);
	}

	/**
	 * User not found exceptions handler.
	 *
	 * @param e UserNotFoundException.
	 * @return http 404 response.
	 */
	@ExceptionHandler(value = UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> userNotFoundExceptionHandler(UserNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
				.body(JSON_START + e.getMessage() + JSON_END);
	}

	/**
	 * This Exception handler translates to HTTP 400.
	 *
	 * @param e ConstraintViolationException.
	 * @return http 400 response.
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> constraintViolationException(ConstraintViolationException e) {

		String errorMessage = e.getConstraintViolations().iterator().next().getMessage().replaceAll("\"", "")
				+ JSON_END;
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
				.body(JSON_START + errorMessage);
	}

	/**
	 * User unauthorised exceptions handler.
	 *
	 * @param e UserUnauthorisedException.
	 * @return http 401 response.
	 */
	@ExceptionHandler(value = UserUnauthorisedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<String> unauthorisedExceptionHandler(UserUnauthorisedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
				.body(JSON_START + e.getMessage() + JSON_END);
	}

	/**
	 * User forbidden exceptions handler.
	 *
	 * @param e UserForbiddenException.
	 * @return http 403 response.
	 */
	@ExceptionHandler(value = UserForbiddenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<String> forbiddenExceptionHandler(UserForbiddenException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
				.body(JSON_START + e.getMessage() + JSON_END);
	}

	/**
	 * This Exception handler translates to HTTP 500.
	 *
	 * @param e HttpServerErrorException.
	 * @return http 500 response.
	 */
	@ExceptionHandler(value = UserInternalServerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> internalServerErrorHandler(UserInternalServerException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(JSON_START + e.getMessage() + JSON_END);
	}

}
