package com.springboot.moviebank.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.springboot.moviebank.domain.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {
			
	List<Movie> findAllByTitleContainsIgnoreCase(String title);
	
	List<Movie> findAllByTitleStartsWithIgnoreCase(String title);

	List<Movie> findAllByTitleEndsWithIgnoreCase(String title);
	
	@Query(value="{ 'Title' : ?0 }")
	List<Movie> findAllByTitle(String title);
	
}
