package com.vevs.controller.validator;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import com.vevs.common.util.VMSUtils;
import com.vevs.common.util.VmsConstants;
import com.vevs.entity.common.Location;
import com.vevs.entity.common.RoleName;
import com.vevs.entity.common.VMSEnum;
import com.vevs.entity.employee.ResetPassword;
import com.vevs.entity.employee.Users;
import com.vevs.entity.vo.ReportRequestVO;
import com.vevs.entity.vo.UserVO;
import com.vevs.entity.vo.VisitorVO;
import com.vevs.entity.visitor.Visitor;
import com.vevs.entity.visitor.VisitorFeedback;
import com.vevs.i18N.MessageByLocaleService;
import com.vevs.repo.LocationRepository;
import com.vevs.repo.ResetPasswordRepository;
import com.vevs.repo.UserRepository;
import com.vevs.repo.VisitorRepository;
import com.vevs.service.AppOTPService;

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
    ResetPasswordRepository passwordRepository;

    @Autowired
    MessageByLocaleService messageSource;

    public List<String> createLocation(Location location) {
        List<String> validateMessage = new ArrayList<>();

        String locName = location.getName();
        String locContactNo = location.getLocationContactNo();
        String country = location.getCountry();
        String address= location.getLocationAddress();
        locName = locName.trim();
        locContactNo = locContactNo.trim();
        country = country.trim();
        address=address.trim();
        if (StringUtils.isEmpty(locName) || StringUtils.isEmpty(locContactNo) || StringUtils.isEmpty(country)) {
            validateMessage.add(messageSource.getMessage("error.location.all.fields.required"));
        }
        if (!validateValueByRegex(locName, VmsConstants.STRING_PATTERN)) {
            validateMessage.add(messageSource.getMessage("error.location.invalid.name"));
        }
        if (!validateMinMaxLengthOfStr(locName, 3, 20)) {
            validateMessage.add(messageSource.getMessage("error.location.name", new Object[] {3, 20}));
        }
        if (!validateMinMaxLengthOfStr(locContactNo, 5, 50)) {
            validateMessage.add(messageSource.getMessage("error.location.contact.number", new Object[] {5, 50}));
        }
        if (!validateMinMaxLengthOfStr(address, 5, 100)) {
            validateMessage.add(messageSource.getMessage("error.location.address", new Object[] {5, 100}));
        }
        if (!validateMinMaxLengthOfStr(country, 3, 30)) {
            validateMessage.add(messageSource.getMessage("error.location.invalid.country", new Object[] {3, 30}));
        }
        return validateMessage;
    }

    public List<String> updateLocation(Location location) {
        List<String> validateMessage = new ArrayList<>();
        Long locId = location.getId();
        if (null == locId) {
            validateMessage.add(messageSource.getMessage("error.location.invalid"));
            return validateMessage;
        }
        Location locationFromDB = locationRepository.getById(locId);
        if (null == locationFromDB) {
            validateMessage.add(messageSource.getMessage("error.location.not.available"));
        }
        createLocation(location);
        return validateMessage;
    }

    public List<String> validateNewUser(UserVO user) {
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
            validateMessage.add(messageSource.getMessage("error.user.invalid.email"));
        }
        if (!validateValueByRegex(mobileNo, VmsConstants.MOBILE_PATTERN)) {
            validateMessage.add(messageSource.getMessage("error.user.invalid.phone.number"));
        }
    /*    if (!validateValueByRegex(mobileNo, VmsConstants.PASSWORD_PATTERN)) {
            String pass = "Password must contain at least one digit[0 - 9]."
                    + "Password must contain at least one lowercase Latin character[ a - z]."
                    + "Password must contain at least one uppercase Latin character[A - Z]."
                    + "Password must contain at least one special character like ! @ # &()."
                    + "Password must contain a length of at least 6 characters and a maximum of 10 characters.";
            validateMessage.add(pass);
        }*/

        if (!EnumUtils.isValidEnum(RoleName.class,user.getRole())) {
            validateMessage.add(messageSource.getMessage("error.user.invalid.role"));
        }
         if(userRepository.existsByEmail(email)){
            validateMessage.add(messageSource.getMessage("error.user.already.exist",new Object[] {email}));
        }
        /*isEmployeeCode Exist
        Users hostEmployee=userRepository.findByEmailAndEnable(email,true);
        if(hostEmployee==null){
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
        if (!validateValueByRegex(visitorName, VmsConstants.STRING_PATTERN)) {
            validateMessage.add(messageSource.getMessage("error.visitor.name.invalid"));
        }
        if (!validateMinMaxLengthOfStr(visitorName, 3, 30)) {
            validateMessage.add(messageSource.getMessage("error.visitor.name", new Object[] {3, 30}));
        }
        if (!validateValueByRegex(visitorEmail, VmsConstants.EMAIL_PATTERN)) {
            validateMessage.add(messageSource.getMessage("error.visitor.email.invalid"));
        }
        if (!validateValueByRegex(mobileNumber, VmsConstants.MOBILE_PATTERN)) {
            validateMessage.add(messageSource.getMessage("error.visitor.mobile.number.invalid"));
        }
        if (!validateMinMaxLengthOfStr(visitorOTP, 6, 6)) {
            validateMessage.add(messageSource.getMessage("error.visitor.otp.invalid"));
        }
        if (!EnumUtils.isValidEnum(VMSEnum.PURPOSE_OF_VISIT.class, purposeOfVisit)) {
            validateMessage.add(messageSource.getMessage("error.visitor.invalid.visitorPurpose"));
        }
       /* if (!validateMinMaxLengthOfStr(purposeOfVisit, 3, 10)) {
            validateMessage.add("Purpose of visit contains only 3 - 10 characters.");
        }*/
        if (!validateMinMaxLengthOfStr(visitorAddress, 5, 30)) {
            validateMessage.add(messageSource.getMessage("error.visitor.address", new Object[] {5, 30}));
        }

        Users hostEmployee = userRepository.getById(hostEmployeeId);
        if (hostEmployee == null) {
            validateMessage.add(messageSource.getMessage("error.visitor.na.hostEmployee"));
        }

        if (!(hostEmployee.getCurrentLocation().getId() == locationId) || !hostEmployee.isEnable()) {
            validateMessage.add(messageSource.getMessage("error.visitor.na.hostEmployee"));
        }


         if (!otpService.isValidOTP(visitorOTP, visitorEmail, mobileNumber)) {
            validateMessage.add(messageSource.getMessage("error.visitor.otp.invalid"));
        }

        return validateMessage;
    }

    public List<String> updateVisitor(Visitor visitor) {
        List<String> validateMessage = new ArrayList<>();
        Long visitorId = visitor.getId();
        if (null == visitorId) {
            validateMessage.add(messageSource.getMessage("error.visitor.invalid"));
            return validateMessage;
        }
        Visitor visitorFromDB = visitorRepository.getById(visitorId);
        if (null == visitorFromDB) {
            validateMessage.add(messageSource.getMessage("error.visitor.invalid"));
        }
        String visitorStatus = visitor.getVisitorStatus();
        if (!EnumUtils.isValidEnum(VMSEnum.VISITOR_STATUS.class, visitorStatus)) {
            validateMessage.add(messageSource.getMessage("error.visitor.status.invalid"));
        }
        return validateMessage;
    }

    public List<String> createUser(Users user) {
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

    public List<String> validateResetPasswordToken(ResetPassword request) {

        List<String> validateMessage = new ArrayList<>();
        if(null==request){
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.invalid.token"));
            return validateMessage;
        }
        ResetPassword resetPassword = passwordRepository.findByToken(request.getToken());
        if(null==resetPassword){
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.invalid.token"));
            return validateMessage;
        }
        if(!StringUtils.equals(request.getUserEmail(),resetPassword.getUserEmail())){
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.invalid.token"));
        }
        Timestamp tokenCreationDate = resetPassword.getTokenCreationTime();
        if (isTokenExpired(tokenCreationDate)) {
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.expire.token"
                    , new Object[] {VmsConstants.UPDATE_PASSWORD_TOKEN_EXPIRE_IN_MINUTES,"minutes"}));
        }

        return validateMessage;
    }



    public List<String> validateEmailForResetPassword(String email) {
        List<String> validateMessage = new ArrayList<>();
        if(StringUtils.isEmpty(email)) {
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.na.email"));
            return validateMessage;
        }
        Optional<Users> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            validateMessage.add(messageSource.getMessage("error.user.reset.pwd.email.not.found", new Object[] {email}));
        }
        return validateMessage;
    }


    private boolean isTokenExpired(final Timestamp tokenCreationTime) {
        Timestamp currentTime= VMSUtils.currentTime();
        Long differenceInMin= VMSUtils.timeDifferenceIn(VmsConstants.MM,tokenCreationTime,currentTime);
        return differenceInMin >= VmsConstants.UPDATE_PASSWORD_TOKEN_EXPIRE_IN_MINUTES;
    }

    public List<String> validateReportRequest(ReportRequestVO request) {
        List<String> validateMessage = new ArrayList<>();


        if(null==request.getFromDate()){
            request.setFromDate(VMSUtils.defaultTime());
        }
        if(null==request.getToDate()){
            request.setToDate(VMSUtils.currentTime());
        }
        if(request.getFromDate().compareTo(request.getToDate())>0) {
            validateMessage.add(messageSource.getMessage("report.error.fromTime.beforeTo.toTime", new Object[] {"From Time","To Time"}));
        }
        Long hostEmployeeIdId=request.getHostEmployeeId();
        if(null!=hostEmployeeIdId){
            Users hostEmployee = userRepository.getById(hostEmployeeIdId);
            if (hostEmployee == null) {
                validateMessage.add(messageSource.getMessage("report.error.na.hostEmployee"));
            }
        }


        return validateMessage;
    }
}

