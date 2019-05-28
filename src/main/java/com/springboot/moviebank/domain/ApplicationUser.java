package com.springboot.moviebank.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("User")
public class ApplicationUser {
	

  @Id
  public ObjectId _id;

  public String username;
  public String password;
  
}
