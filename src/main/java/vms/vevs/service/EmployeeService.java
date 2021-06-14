package vms.vevs.service;

import vms.vevs.entity.employee.Employee;
import vms.vevs.entity.employee.User;

import java.util.List;

public interface EmployeeService {
    Employee employeeById(long id);

    boolean isEmployeeExist(Employee emp);

    Employee addEmployee(Employee emp);

    Employee updateEmployee(Employee existEmp);

    List<Employee> allEmployee();
}
