package com.vevs.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.vevs.common.exception.ResourceNotFoundException;
import com.vevs.common.exception.VmsException;
import com.vevs.common.notification.SmsRequest;
import com.vevs.common.notification.SmsSender;
import com.vevs.controller.validator.Validator;
import com.vevs.entity.common.Role;
import com.vevs.entity.common.RoleName;
import com.vevs.entity.employee.ResetPassword;
import com.vevs.entity.employee.Users;
import com.vevs.entity.vo.*;
import com.vevs.i18N.MessageByLocaleService;
import com.vevs.repo.RoleRepository;
import com.vevs.security.JwtTokenProvider;
import com.vevs.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    MessageByLocaleService messageSource;

    @Autowired
    Validator validator;

    @Autowired
    private  SmsSender service;


    @PostMapping("public/login")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("inside login ");

        HttpResponse<LoginResponse> response = new HttpResponse<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            Users user = userService.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
                    new ResourceNotFoundException("USER", loginRequest.getUsername(), loginRequest.getUsername()));
            Long loggedInUserId = user.getId();
            String role = new VMSHelper().roleOfUser(user);
            response.setResponseObject(new LoginResponse(jwt, loggedInUserId, role));

        } catch (Throwable e) {
            List<String> validationMsgList = new ArrayList<>();
            validationMsgList.add(e.getMessage());
            return HttpResponse.loginErrorResponse(validationMsgList);
        }
        return response;
    }


    @GetMapping(value = "all")
    public HttpResponse<?> listAllUsers(@RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<Users>> response = new HttpResponse<>();
        response.setResponseObject(userService.findAllUsers());
        return response;
    }


    @GetMapping(value = "view/{id}")
    public HttpResponse<?> getUser(@PathVariable("id") long id, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Fetching User with id {}", id);
        HttpResponse<Users> response = new HttpResponse<>();
        response.setResponseObject(userService.findById(id));
        return response;
    }

    @PostMapping(value = "public/newUser")
    public HttpResponse<?> newUser(@RequestBody UserVO user, UriComponentsBuilder ucBuilder,
                                      @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Creating User : {}", user);
        logger.info("loggedInUserId : {}", loggedInUserId);
        HttpResponse<Users> response = new HttpResponse<>();

        try {
            Role userRole =getUserRole(user.getRole());
            List<String> validationMsgList = validator.validateNewUser(user);
            if (!validationMsgList.isEmpty()) {
                return new HttpResponse().errorResponse(validationMsgList);
            }
            response.setResponseObject(userService.saveUser(user, loggedInUserId, userRole));
        } catch (Throwable e) {
            List<String> validationMsgList = new ArrayList<>();
            validationMsgList.add(e.getMessage());
            return HttpResponse.errorResponse(validationMsgList);
        }
        return response;
    }

    @PostMapping(value = "updateUserProfilePhoto")
    public HttpResponse<?> updateUserProfilePhoto( @RequestParam MultipartFile photo
            , @RequestHeader("loggedInUserId") Long loggedInUserId) throws IOException {
        HttpResponse<Users> response = new HttpResponse<>();
        response.setResponseObject(userService.updateUserProfilePhoto(photo, loggedInUserId));
        return response;
    }
    @PutMapping(value = "update")
    public HttpResponse<?> updateUser(@RequestBody Users userTobeUpdate
            , @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<Users> response = new HttpResponse<>();
        response.setResponseObject(userService.updateUser(userTobeUpdate, loggedInUserId));
        return response;
    }

    @GetMapping(value = "public/employeesByLocationId/{locationId}", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> employeesByLocationId(@PathVariable("locationId") long locId
            , @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Fetching User with location Id {}", locId);
        HttpResponse<List<Users>> response = new HttpResponse<>();
        response.setResponseObject(userService.employeesByLocationId(locId));
        return response;
    }

    @PostMapping("public/forgotPassword")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> forgotPassword(@RequestParam String email) {
        HttpResponse<ResetPassword> response = new HttpResponse<>();
        List<String> validationMsgList = validator.validateEmailForResetPassword(email);
        if (!validationMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validationMsgList);
        }
        response.setResponseObject(userService.forgotPassword(email));
        return response;
    }

    @PutMapping("public/resetPassword")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> resetPassword(@RequestBody ResetPassword resetPassword) {
        HttpResponse<ResetPassword> response = new HttpResponse<>();
        List<String> validationMsgList = validator.validateResetPasswordToken(resetPassword);
        if (!validationMsgList.isEmpty()) {
            return new HttpResponse().errorResponse(validationMsgList);
        }
        response.setResponseObject(userService.resetPassword(resetPassword));
        return response;
    }

    @PostMapping("logout")
    public HttpResponse<?> logout(@RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse response = new HttpResponse();
        try {
            response.setResponseObject(Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/checkUsernameAvailability")
    public HttpResponse<IdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String username, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<IdentityAvailability> response = new HttpResponse<>();
        response.setResponseObject(userService.checkUsernameAvailability(username));
        return response;
    }

    @GetMapping("/checkEmailAvailability")
    public HttpResponse<IdentityAvailability> checkEmailAvailability(@RequestParam(value = "email") String email, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<IdentityAvailability> response = new HttpResponse<>();
        response.setResponseObject(userService.checkEmailAvailability(email));
        return response;
    }

    @PostMapping("public/smsOnOTP")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public void sendSms(@Valid @RequestBody SmsRequest smsRequest) {
        service.sendSms(smsRequest);
    }



    Role getUserRole(String roleName) {
        roleName = roleName.toUpperCase();
        Role userRole = null;

        if (StringUtils.equals(roleName, RoleName.USER.name())) {
            userRole = roleRepository.findByName(RoleName.USER)
                    .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));
        } else if (StringUtils.equals(roleName, RoleName.ADMIN.name())) {
            userRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));
        } else if (StringUtils.equals(roleName, RoleName.FRONT_DESK.name())) {
            userRole = roleRepository.findByName(RoleName.FRONT_DESK)
                    .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));
        }
        return userRole;
    }

}
