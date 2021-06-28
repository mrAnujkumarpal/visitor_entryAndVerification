package vms.vevs.service;

import vms.vevs.entity.employee.Employee;

import java.util.List;

public interface EmployeeService {
    Employee employeeById(long id);

    boolean isEmployeeExist(Employee emp);

    Employee addEmployee(Employee emp,Long userId);

    Employee updateEmployee(Employee existEmp,Long userId);

    List<Employee> allEmployee();


    List<Employee> employeesByLocationId(long locId);
}
