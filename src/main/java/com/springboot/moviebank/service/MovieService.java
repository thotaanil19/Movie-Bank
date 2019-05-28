package com.springboot.moviebank.service;

import java.util.List;
import java.util.Optional;

import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.springboot.moviebank.dao.MovieRepository;
import com.springboot.moviebank.dao.morphia.MovieMorphiaRepository;
import com.springboot.moviebank.domain.Movie;
import com.springboot.moviebank.dto.MongoDetails;

@Service
public class MovieService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

	@Autowired
	private MovieRepository movieRepository;

	// For using mongo template
	@Autowired
	private MongoTemplate mongoTemplate;

	// For using Morphia api
	@Autowired
	private MongoClient mongoClient;

	@Value("${spring.data.mongodb.database}")
	private String dbName;

	@Autowired
	private MongoDetails mongoDetails;

	public Movie get(String id) {
		LOGGER.debug("Start: movieService.get(" + id + ")");
		Movie movie = null;
		Optional<Movie> optional = movieRepository.findById(id);
		if (optional.isPresent()) {
			movie = optional.get();
		}
		LOGGER.debug("End: movieService.get(" + id + ")");
		return movie;
	}

	public List<Movie> getAll() {
		LOGGER.debug("Start: movieService.getAll()");
		List<Movie> movies = movieRepository.findAll();
		LOGGER.debug("End: movieService.getAll()");
		return movies;
	}

	public List<Movie> getAllByTitle(String title) {
		LOGGER.debug("Start: movieService.getAllByName()");
		List<Movie> movies = movieRepository.findAllByTitleContainsIgnoreCase(title);
		LOGGER.debug("End: movieService.getAllByName()");
		return movies;
	}

	public Movie save(Movie movie) {
		LOGGER.debug("Start: movieService.save(" + movie + ")");
		Movie movie2 = movieRepository.save(movie);
		LOGGER.debug("End: movieService.save(" + movie + ")");
		return movie2;
	}

	public List<Movie> save(List<Movie> movies) {
		LOGGER.debug("Start: movieService.save(" + movies + ")");
		List<Movie> savedMovies = movieRepository.saveAll(movies);
		LOGGER.debug("End: movieService.save(" + movies + ")");
		return savedMovies;
	}

	public boolean delete(String id) {
		LOGGER.debug("Start: movieService.delete(" + id + ")");
		boolean status = true;
		movieRepository.deleteById(id);
		LOGGER.debug("End: movieService.delete(" + id + ")");
		return status;
	}

	/**
	 * Get all Movies by name : Using MongoTemplate
	 * 
	 * @param tiitle
	 * @return List<Movie>
	 */
	public List<Movie> getAllByNameWithMongoTemplate(String title) {
		return mongoTemplate.find(Query.query(Criteria.where("Title").is(title)), Movie.class);
	}

	/**
	 * Get all Movies by name : Using Morphia Api
	 * 
	 * @param tiitle
	 * @return List<Movie>
	 */
	public List<Movie> getAllByTitleWithMongoMorphia(String title) {
		Morphia morphia = new Morphia();
		// morphia.getMapper().getOptions().setObjectFactory(new
		// ObjenesisObjectFactory());
		// morphia.getMapper().getOptions().setStoreEmpties(true);
		MovieMorphiaRepository movieMorphiaRepository = new MovieMorphiaRepository(mongoClient, morphia,
				mongoDetails.getDatabase());
		return movieMorphiaRepository.createQuery().field("title").containsIgnoreCase(title)
				.asList();
	}

}
