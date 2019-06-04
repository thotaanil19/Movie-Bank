package com.springboot.moviebank.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.moviebank.domain.JwtToken;

@Repository
public interface JwtTokenRepository extends MongoRepository<JwtToken,String> {
}
