package com.springboot.moviebank.dao.morphia;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;
import com.springboot.moviebank.domain.Employee;

public class EmployeeMorphiaRepository extends BasicDAO<Employee, String> {
	
	public EmployeeMorphiaRepository(MongoClient mongo, Morphia morphia, String dbName) {	
        super(mongo,new Morphia(),dbName);
    }

}
