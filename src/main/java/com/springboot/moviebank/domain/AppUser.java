package com.springboot.moviebank.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("User")
public class AppUser {

	@Id
	public ObjectId _id;
	private String name;
	private String email;
	public String username;
	public String password;
	public String role;
	public boolean isActive;

}
