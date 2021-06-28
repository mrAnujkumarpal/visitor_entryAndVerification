package vms.vevs.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.common.exception.VmsException;
import vms.vevs.entity.common.Role;
import vms.vevs.entity.common.RoleName;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.entity.virtualObject.LoginRequest;
import vms.vevs.entity.virtualObject.LoginResponse;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.repo.RoleRepository;
import vms.vevs.security.JwtTokenProvider;
import vms.vevs.service.UserService;
import vms.vevs.service.impl.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collections;
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

    @PostMapping("public/login")
    @ApiImplicitParams({@ApiImplicitParam(name = "loggedInUserId")})
    public HttpResponse<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("inside login ");

        HttpResponse<LoginResponse> response = new HttpResponse<>();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        Users user = userService.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException(messageSource.getMessage("user.error.not.found")));

        Long loggedInUserId = user.getId();
        response.setResponseObject(new LoginResponse(jwt, loggedInUserId));

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

    @PostMapping(value = "create")
    public HttpResponse<?> createUser(@RequestBody Users user, UriComponentsBuilder ucBuilder,
                                      @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Creating User : {}", user);
        logger.info("loggedInUserId : {}", loggedInUserId);
        HttpResponse<Users> response = new HttpResponse<>();

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new VmsException(messageSource.getMessage("user.error.role.not.set")));

        user.setRoles(Collections.singleton(userRole));

        response.setResponseObject(userService.saveUser(user, loggedInUserId));
        return response;
    }


    @PutMapping(value = "update/{id}")
    public HttpResponse<?> updateUser(@PathVariable("id") long id,
                                      @RequestBody Users user, @RequestHeader("loggedInUserId") Long loggedInUserId) {
        logger.info("Updating User with id {}", id);
        HttpResponse<Users> response = new HttpResponse<>();
        Users currentUser = userService.findById(id);


        response.setResponseObject(userService.updateUser(currentUser, loggedInUserId));
        return response;
    }

}