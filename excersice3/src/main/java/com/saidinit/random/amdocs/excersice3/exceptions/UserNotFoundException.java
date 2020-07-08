package com.saidinit.random.amdocs.excersice3.exceptions;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}

	public UserNotFoundException() {
		super("Not found.");
	}
}
