package com.springboot.moviebank.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springboot.moviebank.domain.ApplicationUser;

public interface UserRepository extends MongoRepository<ApplicationUser, String> {

	ApplicationUser findByUsername(String username);
	
}
