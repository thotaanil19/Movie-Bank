package com.springboot.moviebank.dao.morphia;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.springboot.moviebank.domain.Movie;
import com.springboot.moviebank.dto.MongoDetails;

@Repository
public class MovieMorphiaRepository extends BasicDAO<Movie, String> {
	
	@Autowired
	public MovieMorphiaRepository(MongoClient mongo, MongoDetails mongoDetails) {	
        super(mongo,new Morphia(),mongoDetails.getDatabase());
    }

}
