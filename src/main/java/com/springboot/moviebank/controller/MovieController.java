package com.springboot.moviebank.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.moviebank.domain.Movie;
import com.springboot.moviebank.service.MovieService;

@RestController
@RequestMapping("/movie")
public class MovieController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

	@Autowired
	private MovieService movieService;

	@GetMapping("/{id}")
	public ResponseEntity<Movie> get(@PathVariable String id) {
		LOGGER.info("Start: /movie/get/" + id);
		ResponseEntity<Movie> response = null;
		try {
			Movie movie = movieService.get(id);
			response = new ResponseEntity<Movie>(movie, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("End: /movie/get/" + id);
		return response;
	}

	@GetMapping
	public ResponseEntity<List<Movie>> getAll() {
		LOGGER.info("Start: /movie/get");
		ResponseEntity<List<Movie>> response = null;
		try {
			List<Movie> movies = movieService.getAll();
			response = new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<List<Movie>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("End: /movie/get");
		return response;
	}

	@GetMapping("/getAllByTitle/{title}")
	public ResponseEntity<List<Movie>> getAllByTitle(@PathVariable String title) {
		LOGGER.info("Start: /movie/get");
		ResponseEntity<List<Movie>> response = null;
		try {
			List<Movie> movie = movieService.getAllByTitleWithMongoMorphia(title);
			response = new ResponseEntity<List<Movie>>(movie, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<List<Movie>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("End: /movie/get");
		return response;
	}

	@PostMapping
	public ResponseEntity<Movie> save(@RequestBody Movie movie) {
		LOGGER.info("Start: /movie/save");
		ResponseEntity<Movie> response = null;
		try {
			Movie movie2 = movieService.save(movie);
			response = new ResponseEntity<Movie>(movie2, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("End: /movie/save");
		return response;
	}

	@PostMapping("/saveAll")
	public ResponseEntity<List<Movie>> saveAll(@RequestBody List<Movie> movies) {
		LOGGER.info("Start: /movie/saveAll");
		ResponseEntity<List<Movie>> response = null;
		try {
			List<Movie> savedMovies = movieService.save(movies);
			response = new ResponseEntity<List<Movie>>(savedMovies, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<List<Movie>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("End: /movie/save");
		return response;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable String id) {
		LOGGER.info("Start: /movie/delete(" + id + ")");
		ResponseEntity<Boolean> response = null;
		try {
			boolean deleteStatus = movieService.delete(id);
			response = new ResponseEntity<Boolean>(deleteStatus, HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("End: /movie/delete(" + id + ")");
		return response;
	}

}
