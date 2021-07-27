package vms.vevs.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.common.exception.ResourceNotFoundException;
import vms.vevs.common.exception.VmsException;
import vms.vevs.controller.validator.Validator;
import vms.vevs.entity.common.Role;
import vms.vevs.entity.common.RoleName;
import vms.vevs.entity.employee.ResetPassword;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.*;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.repo.RoleRepository;
import vms.vevs.security.JwtTokenProvider;
import vms.vevs.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
               // new UsernameNotFoundException(messageSource.getMessage("error.user.not.found")));
                 new ResourceNotFoundException("USER", loginRequest.getUsername(), loginRequest.getUsername()));
        Long loggedInUserId = user.getId();
        String role=new VMSHelper().roleOfUser(user);
        response.setResponseObject(new LoginResponse(jwt, loggedInUserId,role));

    } catch (Throwable e) {

            HttpResponse.errorResponse(e.getMessage());
        e.printStackTrace();

    }
		return response;
}


    @GetMapping(value = "all")
    public HttpResponse<?> listAllUsers(@RequestHeader("loggedInUserId") Long loggedInUserId) {
        HttpResponse<List<Users>> response = new HttpResponse<>();
        List<Users> usersList = userService.findAllUsers();
        for (Users list : usersList) {
            list.setPassword(StringUtils.EMPTY);
        }
        response.setResponseObject(usersList);
        return response;
    }


    @GetMapping(value = "view/{id}")
    public HttpResponse<?> getUser(@PathVariable("id") long id, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Fetching User with id {}", id);
        HttpResponse<Users> response = new HttpResponse<>();
        Users user = userService.findById(id);
        user.setPassword(StringUtils.EMPTY);
        response.setResponseObject(user);
        return response;
    }

    @PostMapping(value = "public/create")
    public HttpResponse<?> createUser(@RequestBody UserVO user, UriComponentsBuilder ucBuilder,
                                      @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Creating User : {}", user);
        logger.info("loggedInUserId : {}", loggedInUserId);

        HttpResponse<Users> response = new HttpResponse<>();

        try {
            String roleName = user.getRole();
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


            List<String> validationMsgList = validator.validateUser(user);
            if (!validationMsgList.isEmpty()) {
                return new HttpResponse().errorResponse(validationMsgList);
            }
            Users savedUser = userService.saveUser(user, loggedInUserId, userRole);
            savedUser.setPassword(StringUtils.EMPTY);
            response.setResponseObject(savedUser);
        } catch (Throwable e) {

            HttpResponse.errorResponse(e.getMessage());
            e.printStackTrace();

        }
        return response;
    }



    @PutMapping(value = "update/{id}")
    public HttpResponse<?> updateUser(@PathVariable("id") long id,
                                      @RequestBody Users user
            , @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Updating User with id {}", id);
        HttpResponse<Users> response = new HttpResponse<>();
        Users currentUser = userService.findById(id);

        Users updatedUser = userService.updateUser(currentUser, loggedInUserId);
        updatedUser.setPassword(StringUtils.EMPTY);
        response.setResponseObject(updatedUser);
        return response;
    }

    @GetMapping(value = "public/employeesByLocationId/{locationId}",  produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> employeesByLocationId(@PathVariable("locationId") long locId
            ,@RequestHeader("loggedInUserId") Long loggedInUserId) {
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
    public HttpResponse<?> logout(@RequestHeader("loggedInUserId") Long loggedInUserId)
            throws Exception {
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

}
