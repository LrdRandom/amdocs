package com.saidinit.random.amdocs.excersice3.exceptions;

public class UserForbiddenException extends RuntimeException {
	public UserForbiddenException(String errorMessage) {
		super(errorMessage);
	}
}
