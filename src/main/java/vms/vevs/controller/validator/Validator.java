package vms.vevs.controller.validator;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsConstants;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.common.Location;
import vms.vevs.entity.common.VMSEnum;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.VisitorVO;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.entity.visitor.VisitorFeedback;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.repo.LocationRepository;
import vms.vevs.repo.UserRepository;
import vms.vevs.repo.VisitorRepository;
import vms.vevs.service.AppOTPService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Configurable
public class Validator extends ValidatorHelper {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    AppOTPService otpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageByLocaleService messageSource;

    public List<String> createLocation(Location location) {
        List<String> validateMessage = new ArrayList<>();

        String locName = location.getName();
        String locContactNo = location.getLocationContactNo();
        String country = location.getCountry();
        locName = locName.trim();
        locContactNo = locContactNo.trim();
        country = country.trim();
        if (StringUtils.isEmpty(locName) || StringUtils.isEmpty(locContactNo) || StringUtils.isEmpty(country)) {
            validateMessage.add(messageSource.getMessage("error.location.all.fields.required"));

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

    public List<String> updateLocation(Location location) {
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
        createLocation(location);
        return validateMessage;
    }

    public List<String> validateUser(Users user) {
        List<String> validateMessage = new ArrayList<>();

        String name = user.getName();
        String email = user.getEmail();
        String username = user.getUsername();
        String mobileNo = user.getMobileNo();
        String password = user.getPassword();

        name = name.trim();
        email = email.trim();
        username = username.trim();
        mobileNo = mobileNo.trim();
        password = password.trim();


        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email) || StringUtils.isEmpty(username) || StringUtils.isEmpty(mobileNo) || StringUtils.isEmpty(password)) {
            validateMessage.add(messageSource.getMessage("error.user.all.fields.required"));
        }

        if (!validateValueByRegex(email, VmsConstants.EMAIL_PATTERN)) {
            validateMessage.add("Please provide a valid email.");
        }
        if (!validateValueByRegex(mobileNo, VmsConstants.MOBILE_PATTERN)) {
            validateMessage.add("Please provide a valid phone number.");
        }
    /*    if (!validateValueByRegex(mobileNo, VmsConstants.PASSWORD_PATTERN)) {
            String pass = "Password must contain at least one digit[0 - 9]."
                    + "Password must contain at least one lowercase Latin character[ a - z]."
                    + "Password must contain at least one uppercase Latin character[A - Z]."
                    + "Password must contain at least one special character like ! @ # &()."
                    + "Password must contain a length of at least 6 characters and a maximum of 10 characters.";
            validateMessage.add(pass);
        }*/
        Optional<Users> isUserAvail= userRepository.findByUsernameOrEmail(username,email);
        if(isUserAvail.isPresent() ){
            validateMessage.add(messageSource.getMessage("error.user.already.exist"));
        }
        //isEmployeeCode Exist
       /* Employee emp=employeeRepository.findByEmployeeCodeAndEnable(employeeCode,true);
        if(emp!=null){
            validateMessage.add(messageSource.getMessage("error.employee.exist.employeeCode"));
        }
*/
        return validateMessage;
    }


    public List<String> validateVisitor(VisitorVO visitor) {
        List<String> validateMessage = new ArrayList<>();

        String visitorName = visitor.getVisitorName();
        String visitorEmail = visitor.getVisitorEmail();
        String mobileNumber = visitor.getMobileNumber();
        String visitorAddress = visitor.getVisitorAddress();
        String purposeOfVisit = visitor.getPurposeOfVisit(); //PURPOSE_OF_VISIT
       // String visitorImage = visitor.getVisitorImage();

        Long hostEmployeeId = visitor.getHostEmployeeId();
        Long locationId = visitor.getLocationId();

        String visitorOTP = visitor.getVisitorOTP();

        visitorName = visitorName.trim();
        visitorEmail = visitorEmail.trim();
        mobileNumber = mobileNumber.trim();
        visitorAddress = visitorAddress.trim();
        purposeOfVisit = purposeOfVisit.trim();
        //visitorImage = visitorImage.trim();
        visitorOTP = visitorOTP.trim();

        if (StringUtils.isEmpty(visitorName) || StringUtils.isEmpty(mobileNumber)
                || StringUtils.isEmpty(visitorEmail) || StringUtils.isEmpty(visitorAddress)
                || StringUtils.isEmpty(purposeOfVisit) || StringUtils.isEmpty(visitorName)
                || StringUtils.isEmpty(visitorOTP)
                || Objects.isNull(hostEmployeeId) || Objects.isNull(locationId)) {
            validateMessage.add(messageSource.getMessage("error.visitor.all.fields.required"));
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
        if (!validateMinMaxLengthOfStr(visitorOTP, 6, 6)) {
            validateMessage.add("OTP contains only 6 characters.");
        }
        if (!EnumUtils.isValidEnum(VMSEnum.PURPOSE_OF_VISIT.class, purposeOfVisit)) {
            validateMessage.add(messageSource.getMessage("error.visitor.invalid.visitorPurpose"));
        }
       /* if (!validateMinMaxLengthOfStr(purposeOfVisit, 3, 10)) {
            validateMessage.add("Purpose of visit contains only 3 - 10 characters.");
        }*/
        if (!validateMinMaxLengthOfStr(visitorAddress, 5, 30)) {
            validateMessage.add("Address contains only 5 - 30 characters.");
        }

        Users hostEmployee = userRepository.getById(hostEmployeeId);
        if (hostEmployee == null) {
            validateMessage.add(messageSource.getMessage("error.visitor.na.hostEmployee"));
        }

        if (!(hostEmployee.getCurrentLocation().getId() == locationId) || !hostEmployee.isEnable()) {
            validateMessage.add(messageSource.getMessage("error.visitor.na.hostEmployee"));
        }


       /* if (!otpService.isValidOTP(visitorOTP, visitorEmail, mobileNumber)) {
            validateMessage.add(messageSource.getMessage("error.visitor.otp.invalid"));
        }*/

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

    public List<String> createUser(Users user, MessageByLocaleService messageSource) {
        List<String> validateMessage = new ArrayList<>();

        String name = user.getName();
        String email = user.getEmail();
        String username = user.getUsername();
        String mobileNo = user.getMobileNo();
        String password = user.getPassword();

        name = name.trim();
        email = email.trim();
        username = username.trim();
        mobileNo = mobileNo.trim();
        password = password.trim();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(email) || StringUtils.isEmpty(username) || StringUtils.isEmpty(mobileNo) || StringUtils.isEmpty(password)) {
            validateMessage.add(messageSource.getMessage("error.user.all.fields.required"));
        }
       Optional<Users> users= userRepository.findByUsernameOrEmail(username,email);
        if(null!=users){
            validateMessage.add(messageSource.getMessage("error.user.already.exist"));
        }



        return validateMessage;
    }

    public List<String> createFeedback(VisitorFeedback feedback) {
        List<String> validateMessage = new ArrayList<>();
        Long visitorId=feedback.getVisitorId();

        String remarks=feedback.getRemarkPoints();
        String finalFeedback=feedback.getFeedback();

        remarks = remarks.trim();
        finalFeedback = finalFeedback.trim();

        if (!validateMinMaxLengthOfStr(remarks, 6, 16)) {
            validateMessage.add(messageSource.getMessage("error.feedback.remarks.length", new Object[] {6, 16} ));
        }
        if (!validateMinMaxLengthOfStr(finalFeedback, 6, 400)) {
            validateMessage.add(messageSource.getMessage("error.feedback.finalFeedback.length", new Object[] {6, 400}));
        }

        return validateMessage;
    }

    public List<String> validateResetPasswordToken(String token) {

        List<String> validateMessage = new ArrayList<>();
        Users user = userRepository.findByToken(token);
        Timestamp tokenCreationDate = user.getTokenCreationTime();
        if (isTokenExpired(tokenCreationDate)) {
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.invalid.token"));
        }
        return validateMessage;
    }



    public List<String> validateEmailForResetPassword(String email) {
        List<String> validateMessage = new ArrayList<>();

        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            validateMessage.add(messageSource.getMessage("error.user.email.not.found", new Object[] {email}));
        }
        return validateMessage;
    }


    private boolean isTokenExpired(final Timestamp tokenCreationTime) {
        Timestamp now= VmsUtils.currentTime();
        Long differenceInMin=VmsUtils.timeDifferenceIn("MM",tokenCreationTime,now);
        return differenceInMin >= VmsConstants.UPDATE_PASSWORD_TOKEN_EXPIRE_IN_MINUTES;
    }

    /**/
}

