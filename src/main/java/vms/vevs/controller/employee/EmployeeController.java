package vms.vevs.controller.employee;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.employee.Employee;
import vms.vevs.service.EmployeeService;

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

        List<String>  validateEmployee=   new Validator().createEmployee(emp);
        if (validateEmployee.size()>0){
            logger.info(validateEmployee.toString());
        }
        return employeeService.addEmployee(emp);

    }


    @RequestMapping(value = "update", method = RequestMethod.PUT, produces = "application/json")
    public Employee updateEmployee(@RequestBody Employee emp) {
        logger.info("Updating employee with id {}", emp.toString());


        List<String>  validateEmployee=   new Validator().updateEmployee(emp);
        if (validateEmployee.size()>0){
            logger.info(validateEmployee.toString());
        }




        return employeeService.updateEmployee(emp);

    }

}
