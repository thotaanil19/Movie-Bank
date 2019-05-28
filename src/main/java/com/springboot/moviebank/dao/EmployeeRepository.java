package com.springboot.moviebank.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.springboot.moviebank.domain.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
			
	List<Employee> findAllByNameContainsIgnoreCase(String name);
	
	List<Employee> findAllByNameStartsWithIgnoreCase(String name);

	List<Employee> findAllByNameEndsWithIgnoreCase(String name);
	
	@Query(value="{ 'name' : ?0 }")
	List<Employee> findAllByName(String name);
	
}
