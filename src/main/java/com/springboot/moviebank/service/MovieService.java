package com.springboot.moviebank.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.moviebank.dao.MovieRepository;
import com.springboot.moviebank.dao.morphia.MovieMorphiaRepository;
import com.springboot.moviebank.domain.Movie;

@Service
public class MovieService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MovieMorphiaRepository movieMorphiaRepository;

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
	 * Get all Movies by name : Using Morphia Api
	 * 
	 * @param title
	 * @return List<Movie>
	 */
	public List<Movie> getAllByTitleWithMongoMorphia(String title) {
		/*
		 * Morphia morphia = new Morphia(); MovieMorphiaRepository
		 * movieMorphiaRepository = new MovieMorphiaRepository(mongoClient, morphia,
		 * mongoDetails.getDatabase());
		 */
		return movieMorphiaRepository.createQuery().field("title").containsIgnoreCase(title).asList();
	}
	
	/**
	 * Get all Movies by year : Using Morphia Api
	 * 
	 * @param year
	 * @return List<Movie>
	 */
	public List<Movie> getAllByYearWithMongoMorphia(int year) {
		return movieMorphiaRepository.createQuery().field("year").equal(year).asList();
	}
	
	/**
	 * Get all Movies by Director : Using Morphia Api
	 * 
	 * @param director
	 * @return List<Movie>
	 */
	public List<Movie> getAllByDirectorWithMongoMorphia(String director) {
		return movieMorphiaRepository.createQuery().field("directors").containsIgnoreCase(director).asList();
	}
	
	/**
	 * Get all Movies by actor : Using Morphia Api
	 * 
	 * @param actor
	 * @return List<Movie>
	 */
	public List<Movie> getAllByActorWithMongoMorphia(String actor) {
		return movieMorphiaRepository.createQuery().field("actors").containsIgnoreCase(actor).asList();
	}
	
	/**
	 * Get all Movies by language : Using Morphia Api
	 * 
	 * @param language
	 * @return List<Movie>
	 */
	public List<Movie> getAllByLanguageWithMongoMorphia(String language) {
		return movieMorphiaRepository.createQuery().field("languages").containsIgnoreCase(language).asList();
	}
	
	/**
	 * Get all Movies by genre : Using Morphia Api
	 * 
	 * @param genre
	 * @return List<Movie>
	 */
	public List<Movie> getAllByGenreWithMongoMorphia(String genre) {
		return movieMorphiaRepository.createQuery().field("genres").containsIgnoreCase(genre).asList();
	}
	
	/**
	 * Get all Movies by country : Using Morphia Api
	 * 
	 * @param country
	 * @return List<Movie>
	 */
	public List<Movie> getAllByCountryWithMongoMorphia(String country) {
		return movieMorphiaRepository.createQuery().field("country").containsIgnoreCase(country).asList();
	}

}
