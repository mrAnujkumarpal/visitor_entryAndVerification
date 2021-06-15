package vms.vevs.controller.employee;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.employee.Employee;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employee/")
public class EmployeeController {

    public static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;


    @GetMapping(value = "all", produces = "application/json")
    public HttpResponse<?> listAllEmployee() {
        HttpResponse<List<Employee>> response = new HttpResponse<>();
        response.setResponseObject(employeeService.allEmployee());
        return response;

    }


    @GetMapping(value = "view/{id}",  produces = "application/json")
    public HttpResponse<?> employeeById(@PathVariable("id") long id) {
        logger.info("Fetching User with id {}", id);
        HttpResponse<Employee> response = new HttpResponse<>();
        response.setResponseObject(employeeService.employeeById(id));
        return response;
    }

    @PostMapping(value = "create",  produces = "application/json")
    public HttpResponse<?> createEmployee(@RequestBody Employee emp) {
        logger.info("Creating employee : {}", emp);
        HttpResponse<Employee> response = new HttpResponse<>();
        List<String> validationMsgList = new Validator().createEmployee(emp);
        if (!validationMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validationMsgList);
        }
        response.setResponseObject(employeeService.addEmployee(emp));
        return response;
    }


    @PutMapping(value = "update",  produces = "application/json")
    public HttpResponse<?> updateEmployee(@RequestBody Employee emp) {
        logger.info("Updating employee with id {}", emp);
        HttpResponse<Employee> response = new HttpResponse<>();
        List<String> validationMsgList = new Validator().updateEmployee(emp);
        if (!validationMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validationMsgList);
        }
        response.setResponseObject(employeeService.updateEmployee(emp));
        return response;
    }

}
