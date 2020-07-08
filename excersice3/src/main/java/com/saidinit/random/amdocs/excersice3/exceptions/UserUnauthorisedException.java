package com.saidinit.random.amdocs.excersice3.exceptions;

public class UserUnauthorisedException extends RuntimeException {
	public UserUnauthorisedException(String errorMessage) {
		super(errorMessage);
	}
}
