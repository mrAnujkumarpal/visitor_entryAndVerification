package vms.vevs.controller.employee;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.common.exception.handler.CustomErrorType;
import vms.vevs.common.util.JWTUtility;
import vms.vevs.controller.user.UserController;
import vms.vevs.entity.employee.Employee;
import vms.vevs.entity.employee.User;
import vms.vevs.entity.virtualObject.JWTRequest;
import vms.vevs.entity.virtualObject.JWTResponse;
import vms.vevs.service.EmployeeService;
import vms.vevs.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/employee/")
public class EmployeeController {
    public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    EmployeeService employeeService;


    @RequestMapping(value = "all", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<List<Employee>> listAllEmployee() {
        List<Employee> employees =
                employeeService.allEmployee();
        if (employees.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Employee>>(employees,
                HttpStatus.OK);
    }


    @RequestMapping(value = "view/{id}", method = RequestMethod.GET, produces = "application/json")
    public Employee employeeById(@PathVariable("id") long id) {
        logger.info("Fetching User with id {}", id);

        Employee employee = employeeService.employeeById(id);
        if (employee == null) {
        }
        return employee;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "application/json")
    public Employee createEmployee(@RequestBody Employee emp) {
        logger.info("Creating employee : {}", emp);

        if (employeeService.isEmployeeExist(emp)) {

        }
        return employeeService.addEmployee(emp);

    }


    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT, produces = "application/json")
    public Employee updateEmployee(@PathVariable("id") long id, @RequestBody Employee emp) {
        logger.info("Updating employee with id {}", id);

        Employee existEmp = employeeService.employeeById(id);

        if (existEmp == null) {
            logger.error("Unable to update. User with id {} not found.", id);
        }

        existEmp.setName(emp.getName());


        return employeeService.updateEmployee(existEmp);

    }

}
