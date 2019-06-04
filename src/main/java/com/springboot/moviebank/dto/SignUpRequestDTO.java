package com.springboot.moviebank.dto;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String username;
	private String password;
	private String role;
}
