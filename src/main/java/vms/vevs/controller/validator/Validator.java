package vms.vevs.controller.validator;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.common.util.VmsConstants;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.entity.employee.Employee;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.repo.EmployeeRepository;
import vms.vevs.repo.LocationRepository;
import vms.vevs.repo.VisitorRepository;
import vms.vevs.service.AppOTPService;

import java.util.ArrayList;
import java.util.List;
@Component
public class Validator extends ValidatorHelper {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    AppOTPService otpService;



    public List<String> createLocation(Location location,MessageByLocaleService messageSource) {
        List<String> validateMessage = new ArrayList<>();

        String locName = location.getName();
        String locContactNo = location.getLocationContactNo();
        String country=location.getCountry();
        locName = locName.trim();
        locContactNo = locContactNo.trim();
        country=country.trim();
        if (StringUtils.isEmpty(locName) || StringUtils.isEmpty(locContactNo) || StringUtils.isEmpty(country)) {
            validateMessage.add(messageSource.getMessage("all.fields.required"));

        }
        if (!validateMinMaxLengthOfStr(locName, 3, 20)) {
            validateMessage.add("Name contains only 3 - 20 characters.");
        }
        if (!validateValueByRegex(locName, VmsConstants.STRING_PATTERN)) {
            validateMessage.add("Only String allowed in name.");
        }
        if (!validateMinMaxLengthOfStr(locContactNo, 5, 30)) {
            validateMessage.add("Location contact number contains only 5 - 30 characters.");
        }
        if (!validateMinMaxLengthOfStr(country, 3, 30)) {
            validateMessage.add("Location contact number contains only 3 - 30 characters.");
        }
        return validateMessage;
    }

    public List<String> updateLocation(Location location,MessageByLocaleService messageSource) {
        List<String> validateMessage = new ArrayList<>();
        Long locId = location.getId();
        if (null == locId) {
            validateMessage.add("Please provide location id to update location.");
            return validateMessage;
        }
        Location locationFromDB = locationRepository.getById(locId);
        if (null == locationFromDB) {
            validateMessage.add("Please provide a valid location id to update location.");
        }
        createLocation(location,messageSource);
        return validateMessage;
    }

    public List<String> validateUser(Users user) {
        List<String> validateMessage = new ArrayList<>();
        String username = user.getUsername();
        String mobileNo = user.getMobileNo();
        String password = user.getPassword();
        username = username.trim();
        mobileNo = mobileNo.trim();
        password = password.trim();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(mobileNo) || StringUtils.isEmpty(password)) {

            validateMessage.add("All fields required.");
            return validateMessage;
        }
        if (!validateValueByRegex(username, VmsConstants.EMAIL_PATTERN)) {
            validateMessage.add("Please provide a valid email.");
        }
        if (!validateValueByRegex(mobileNo, VmsConstants.MOBILE_PATTERN)) {
            validateMessage.add("Please provide a valid phone number.");
        }
        if (!validateValueByRegex(mobileNo, VmsConstants.PASSWORD_PATTERN)) {
            String pass = "Password must contain at least one digit[0 - 9]."
                    + "Password must contain at least one lowercase Latin character[ a - z]."
                    + "Password must contain at least one uppercase Latin character[A - Z]."
                    + "Password must contain at least one special character like ! @ # &()."
                    + "Password must contain a length of at least 6 characters and a maximum of 10 characters.";
            validateMessage.add(pass);
        }


        return validateMessage;
    }


    public List<String> createEmployee(Employee employee) {
        List<String> validateMessage = new ArrayList<>();

        String employeeName = employee.getName();
        String employeeCode = employee.getEmployeeCode();
        String mobileNumber = employee.getMobileNumber();
        String designation = employee.getDesignation();
        String emailId = employee.getEmailId();
        employeeCode = employeeCode.trim();
        mobileNumber = mobileNumber.trim();
        designation = designation.trim();
        emailId = emailId.trim();
        if (StringUtils.isEmpty(employeeCode) || StringUtils.isEmpty(mobileNumber)
                || StringUtils.isEmpty(designation) || StringUtils.isEmpty(emailId)
                || StringUtils.isEmpty(employeeName)) {

            validateMessage.add("All fields required.");
            return validateMessage;
        }
        if (!validateMinMaxLengthOfStr(employeeName, 3, 20)) {
            validateMessage.add("Name contains only 3 - 20 characters.");
        }
        if (!validateValueByRegex(employeeName, VmsConstants.STRING_PATTERN)) {
            validateMessage.add("Only String allowed in name.");
        }
        if (!validateValueByRegex(emailId, VmsConstants.EMAIL_PATTERN)) {
            validateMessage.add("Please provide a valid email.");
        }
        if (!validateValueByRegex(mobileNumber, VmsConstants.MOBILE_PATTERN)) {
            validateMessage.add("Please provide a valid phone number.");
        }
        if (!validateMinMaxLengthOfStr(designation, 5, 30)) {
            validateMessage.add("Designation contains only 5 - 30 characters.");
        }
        if (!validateMinMaxLengthOfStr(employeeCode, 3, 10)) {
            validateMessage.add("Employee code contains only 3 - 10 characters.");
        }
        return validateMessage;
    }

    public List<String> updateEmployee(Employee emp) {
        List<String> validateMessage = new ArrayList<>();

        Long empId = emp.getId();
        if (null == empId) {
            validateMessage.add("Please provide employee id to update location.");
            return validateMessage;
        }
        Employee empFromDB = employeeRepository.getById(empId);
        if (null == empFromDB) {
            validateMessage.add("Please provide a valid employee id to update employee.");
        }
        createEmployee(emp);
        return validateMessage;
    }


    public List<String> validateVisitor(VisitorVO visitor) {
        List<String> validateMessage = new ArrayList<>();

        String visitorName = visitor.getVisitorName();
        String visitorEmail = visitor.getVisitorEmail();
        String mobileNumber = visitor.getMobileNumber();
        String visitorAddress = visitor.getVisitorAddress();
        String purposeOfVisit = visitor.getPurposeOfVisit(); //PURPOSE_OF_VISIT
        String visitorImage = visitor.getVisitorImage();

        Long hostEmployeeId = visitor.getHostEmployeeId();
        Long locationId = visitor.getLocationId();

        String visitorOTP = visitor.getVisitorOTP();

        visitorName = visitorName.trim();
        visitorEmail = visitorEmail.trim();
        mobileNumber = mobileNumber.trim();
        visitorAddress = visitorAddress.trim();
        purposeOfVisit = purposeOfVisit.trim();
        visitorImage = visitorImage.trim();
        visitorOTP = visitorOTP.trim();

        if (StringUtils.isEmpty(visitorName) || StringUtils.isEmpty(mobileNumber)
                || StringUtils.isEmpty(visitorEmail) || StringUtils.isEmpty(visitorAddress)
                || StringUtils.isEmpty(purposeOfVisit) || StringUtils.isEmpty(visitorImage)
                || StringUtils.isEmpty(visitorOTP)) {

            validateMessage.add("All fields required.");
            return validateMessage;
        }
        if (!validateMinMaxLengthOfStr(visitorName, 3, 20)) {
            validateMessage.add("Name contains only 3 - 20 characters.");
        }
        if (!validateValueByRegex(visitorName, VmsConstants.STRING_PATTERN)) {
            validateMessage.add("Only String allowed in name.");
        }
        if (!validateValueByRegex(visitorEmail, VmsConstants.EMAIL_PATTERN)) {
            validateMessage.add("Please provide a valid email.");
        }
        if (!validateValueByRegex(mobileNumber, VmsConstants.MOBILE_PATTERN)) {
            validateMessage.add("Please provide a valid phone number.");
        }
        if (!validateMinMaxLengthOfStr(visitorOTP, 5, 30)) {
            validateMessage.add("OTP contains only 5 - 30 characters.");
        }
        if (!validateMinMaxLengthOfStr(purposeOfVisit, 3, 10)) {
            validateMessage.add("Purpose of visit contains only 3 - 10 characters.");
        }
        if (!validateMinMaxLengthOfStr(visitorAddress, 3, 50)) {
            validateMessage.add("Address contains only 3 - 50 characters.");
        }
        if (!otpService.isValidOTP(visitorOTP, visitorEmail, mobileNumber)) {
            validateMessage.add("Please provide a valid OTP, Check your mailbox.");
        }

        return validateMessage;
    }

    public List<String> updateVisitor(Visitor visitor) {
        List<String> validateMessage = new ArrayList<>();
        Long visitorId = visitor.getId();
        if (null == visitorId) {
            validateMessage.add("Please provide visitor id to update visitor.");
            return validateMessage;
        }
        Visitor visitorFromDB = visitorRepository.getById(visitorId);
        if (null == visitorFromDB) {
            validateMessage.add("Please provide a valid visitor id to update visitor.");
        }
        String visitorStatus = visitor.getVisitorStatus();
        if (!EnumUtils.isValidEnum(VMSEnum.VISITOR_STATUS.class, visitorStatus)) {
            validateMessage.add("Please provide a valid status.");
        }
        return validateMessage;
    }

    /**/
}
