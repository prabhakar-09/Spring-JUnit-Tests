package com.spring.project.testing;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import lombok.val;

/*The JUnit executions happens in different transaction for every method 
even tho we add the ordering of execution this won't make much of the difference
until we bring the execution of all methods into a single transaction.
Therefore adding the @Rollback annotation for certain functions to disable rollback would help*/



// This test only focuses on the persistent layer & repository layer
@DataJpaTest // this will only scan only spring data JPA repositories and JPA entities.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // helpful to maintain the execution order of the test cases
public class EmployeeRepositoryTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	
//	Junit test for save employee
	@Test // Makes this method a Junit test
	@Order(1)
	@Rollback(value = false)
	public void saveEmployeeTest() {
		
		 Employee employee = Employee.builder()
		            .firstName("Peeku")
		            .lastName("Neeku")
		            .email("peeku.neeku@gmole .com")
		            .build();
		 
		 employeeRepository.save(employee); // saving to the DB
		 
		 Assertions.assertThat(employee.getId()).isGreaterThan(0);
	}
	
//	Unit test for get employee
	@Test
	@Order(2)
	public void getEmployee() {
		
		Employee employee = employeeRepository.findById(1L).get(); // Assuming first record contains 1 as ID
		
		Assertions.assertThat(employee.getId()).isEqualTo(1L); // If this condition is true JUnit test will pass
	}
	
//	Unit test to retrieve all employees
	@Test
	@Order(3)
	public void getListOfEmployeesTest() {
		
		List<Employee> employees = employeeRepository.findAll();
		
		Assertions.assertThat(employees.size()).isGreaterThan(0); // if it's greater than 0 then test will pass
	}
	
//	Unit test to check for update employee
	@Test
	@Order(4)
	@Rollback(value = false)
	public void updateEmployeeTest() {
//		In order to update the employee we first need to get the employee
//		
		Employee employee = employeeRepository.findById(1L).get();
		
		employee.setEmail("peekunewemail@gmail.com");
		
		Employee employeeUpdatedObj = employeeRepository.save(employee);
		
		Assertions.assertThat(employeeUpdatedObj.getEmail()).isEqualTo("peekunewemail@gmail.com");
		
	}
	
//	Creating a delete employee 
	@Test
	@Order(5)
	@Rollback(value = false) // Add roll-back condition only to those which will have impact on db like write ops
	public void deleteEmployeeTest() {
		
//		Getting the existing employee first.
//		Employee employee = employeeRepository.findById(1L).orElseThrow(null); // as the optional may return null
//		or
		Employee employee = employeeRepository.findById(1L).get();
		
//		Deleting an employee
		employeeRepository.delete(employee);
//		or
//		employeeRepository.deleteById(1L);
		
//		now trying to retrieve the employee after deleting 
		Employee employee1 = null;
		Optional<Employee> emp = employeeRepository.findByEmail("peekunewemail@gmail.com"); // returns null if not found
						
		if(emp.isPresent()) {
			employee1 = emp.get(); // if not null storing 
		}
		
		Assertions.assertThat(employee1).isNull(); // checks that the employee1 obj should be null for delete condition.
		
	}
	
//	all the above test cases will fail except for save employee because JUnit doesn't maintain the execution order.
}
