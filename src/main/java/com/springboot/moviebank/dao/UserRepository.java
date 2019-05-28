package com.springboot.moviebank.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springboot.moviebank.domain.AppUser;

public interface UserRepository extends MongoRepository<AppUser, String> {

	AppUser findByUsername(String username);
	
}
