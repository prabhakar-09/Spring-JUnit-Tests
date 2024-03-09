package com.spring.project.testing;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	/*
	 * Optional<Employee>: This indicates that the method will return an Optional
	 * container that may or may not contain an Employee object. Using Optional is a
	 * way to handle cases where the result may be null or not found in the database
	 * without risking a NullPointerException. The caller of this method can then
	 * use methods like orElse() or orElseThrow() to handle the case when no result
	 * is found.
	 */
	
	Optional<Employee> findByEmail(String email);
}
