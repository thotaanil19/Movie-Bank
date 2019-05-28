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
import com.springboot.moviebank.dao.EmployeeRepository;
import com.springboot.moviebank.dao.morphia.EmployeeMorphiaRepository;
import com.springboot.moviebank.domain.Employee;
import com.springboot.moviebank.dto.MongoDetails;

@Service
public class EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;

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

	public Employee get(String id) {
		LOGGER.debug("Start: employeeService.get(" + id + ")");
		Employee emp = null;
		Optional<Employee> optional = employeeRepository.findById(id);
		if (optional.isPresent()) {
			emp = optional.get();
		}
		LOGGER.debug("End: employeeService.get(" + id + ")");
		return emp;
	}

	public List<Employee> getAll() {
		LOGGER.debug("Start: employeeService.getAll()");
		List<Employee> employees = employeeRepository.findAll();
		LOGGER.debug("End: employeeService.getAll()");
		return employees;
	}

	public List<Employee> getAllByName(String name) {
		LOGGER.debug("Start: employeeService.getAllByName()");
		List<Employee> employees = employeeRepository.findAllByName(name);
		LOGGER.debug("End: employeeService.getAllByName()");
		return employees;
	}

	public Employee save(Employee emp) {
		LOGGER.debug("Start: employeeService.save(" + emp + ")");
		Employee emp2 = employeeRepository.save(emp);
		LOGGER.debug("End: employeeService.save(" + emp + ")");
		return emp2;
	}

	public boolean delete(String id) {
		LOGGER.debug("Start: employeeService.delete(" + id + ")");
		boolean status = true;
		employeeRepository.deleteById(id);
		LOGGER.debug("End: employeeService.delete(" + id + ")");
		return status;
	}
	
	/**
	 * Get all Employees by name : Using MongoTemplate
	 * @param name
	 * @return List<Employee>
	 */
	public List<Employee> getAllByNameWithMongoTemplate(String name) {
		return mongoTemplate.find(Query.query(Criteria.where("name").is(name)), Employee.class);
	}
	
	/**
	 * Get all Employees by name : Using Morphia Api
	 * @param name
	 * @return List<Employee>
	 */
	public List<Employee> getAllByNameWithMongoMorphia(String name) {
		Morphia morphia = new Morphia();
		// morphia.getMapper().getOptions().setObjectFactory(new ObjenesisObjectFactory());
		// morphia.getMapper().getOptions().setStoreEmpties(true);
		EmployeeMorphiaRepository employeeMorphiaRepository = new EmployeeMorphiaRepository(mongoClient, morphia, mongoDetails.getDatabase());
		return employeeMorphiaRepository.createQuery().field("name").containsIgnoreCase(name).asList();
	}
	
	
	

}
