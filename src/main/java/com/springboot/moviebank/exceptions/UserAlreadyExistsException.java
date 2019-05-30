package com.springboot.moviebank.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -917561642292105324L;
	
	private String username;

	public UserAlreadyExistsException(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

}
