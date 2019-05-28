package com.springboot.moviebank.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * 
 * @author Anil
 * 
 * Domain class to map Employee collection.
 * Remove @Data annotation and add setters and getters if lombok is not installed
 *
 */
@Data
@Document("Employee")
public class Employee {
	
	@Id
	@Field("_id")
	@org.mongodb.morphia.annotations.Id
	private String _id;
	
	@Field("name")
	private String name;
	
	@Field("salary")
	private Double salary;
	
	@Field("phones")
	private List<String> phones;	
	

}
