package com.springboot.moviebank.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="spring.data.mongodb")
@Component
public class MongoDetails {

	private String uri;
	private String database;
	
}
