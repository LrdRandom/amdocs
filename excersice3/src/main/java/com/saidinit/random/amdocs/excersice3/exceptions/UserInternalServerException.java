package com.saidinit.random.amdocs.excersice3.exceptions;

public class UserInternalServerException extends RuntimeException {
	public UserInternalServerException(String errorMessage) {
		super(errorMessage);
	}
}