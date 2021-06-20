package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.employee.Employee;
import vms.vevs.repo.EmployeeRepository;
import vms.vevs.repo.LocationRepository;
import vms.vevs.service.EmployeeService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
        Employee emploee = employeeRepository.findByName(emp.getName());
        if (emploee != null) {
            return true;
        }
        return false;
    }

    @Override
    public Employee addEmployee(Employee emp) {
        emp.setCreatedOn(VmsUtils.currentTime());
        return employeeRepository.save(emp);

    }

    @Override
    public Employee updateEmployee(Employee employee) {

        Employee empFromDB = employeeRepository.getById(employee.getId());

        empFromDB.setModifiedOn(VmsUtils.currentTime());
        empFromDB.setDesignation(employee.getDesignation());
        empFromDB.setMobileNumber(employee.getMobileNumber());
        empFromDB.setEmployeeImage(employee.getEmployeeImage());
        empFromDB.setCurrentLocation(employee.getCurrentLocation());

        return employeeRepository.save(empFromDB);
    }

    @Override
    public List<Employee> allEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> employeesByLocationId(long locId) {
        List<Employee> empList = allEmployee();
        return empList.stream()
                .filter(x -> (x.getBaseLocation().getId()).equals(locId))
                .collect(Collectors.toList());

    }
}
