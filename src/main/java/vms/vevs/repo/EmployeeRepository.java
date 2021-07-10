package vms.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vms.vevs.entity.employee.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Employee findByName(String name);


    Employee findByEmployeeCodeAndEnable(String employeeCode, boolean enable);


   // Employee findByEmailIdAndPasswordAndEnable(String emailId, String password, boolean enable);



    /*List<Employee> findByEmailAddressAndLastname(Employee emailAddress, String lastname);

    // Enables the distinct flag for the query
    List<Employee> findDistinctPeopleByLastnameOrFirstname(String lastname, String firstname);
    List<Employee> findPeopleDistinctByLastnameOrFirstname(String lastname, String firstname);

    // Enabling ignoring case for an individual property
    List<Employee> findByLastnameIgnoreCase(String lastname);
    // Enabling ignoring case for all suitable properties
    List<Employee> findByLastnameAndFirstnameAllIgnoreCase(String lastname, String firstname);

    // Enabling static ORDER BY for a query
    List<Employee> findByLastnameOrderByFirstnameAsc(String lastname);
    List<Employee> findByLastnameOrderByFirstnameDesc(String lastname);

     */
}
