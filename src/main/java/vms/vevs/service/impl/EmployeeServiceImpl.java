package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.employee.Employee;
import vms.vevs.repo.EmployeeRepository;
import vms.vevs.repo.LocationRepository;
import vms.vevs.service.EmployeeService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    LocationRepository locationRepository;

    @Override
    public Employee employeeById(long id) {
        return employeeRepository.getById(id);
    }

    @Override
    public boolean isEmployeeExist(Employee emp) {
        Employee emploee= employeeRepository.findByName(emp.getName());
        if(emploee!=null){
            return true;
        }
        return false;
    }

    @Override
    public Employee addEmployee(Employee emp) {
        emp.setCreatedOn(VmsUtils.currentTime());
        return  employeeRepository.save(emp);

    }

    @Override
    public Employee updateEmployee(Employee employee) {

        employee.setCreatedOn(VmsUtils.currentTime());
        return  employeeRepository.save(employee);
    }

    @Override
    public List<Employee> allEmployee() {
        return employeeRepository.findAll();
    }
}
