package com.springboot.moviebank.domain;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * 
 * @author Anil
 * 
 * Domain class to map Movie collection.
 * Remove @Data annotation and add setters and getters if lombok is not installed
 * 
 * Note: 
 * 1. Use @Entity, @Property annotations if we use mongo morphia api. 
 * 2. Use @Document, @Field annotation if we use spring jpa repositories.
 * 
 */
@Data
@Document("Movie")
@Entity("Movie")
public class Movie {
	
	@Id
	@Field("_id")
	@org.mongodb.morphia.annotations.Id
	private String _id;
	
	@Field("Title")
	@Property("Title")
	private String title;
	
	@Field("Year")
	@Property("Year")
	private Integer year;
	
	@Field("Genres")
	@Property("Genres")
	private List<String> genres;
	
	@Field("Actors")
	@Property("Actors")
	private List<String> actors;
	
	@Field("Languages")
	@Property("Languages")
	private List<String> languages;
	
	@Field("Directors")
	@Property("Directors")
	private List<String> directors;
	
	@Field("Poster")
	@Property("Poster")
	private String poster;
	
	@Field("ReleaseDate")
	@Property("ReleaseDate")
	private String releaseDate;
	
	@Field("Runtime")
	@Property("Runtime")
	private String runtime;
	
	
	@Field("Country")
	@Property("Country")
	private String country;
	
	
		

}
