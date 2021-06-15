package vms.vevs.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vms.vevs.common.exception.handler.CustomErrorType;
import vms.vevs.common.util.JWTUtility;
import vms.vevs.entity.employee.User;
import vms.vevs.entity.virtualObject.HttpResponse;
import vms.vevs.entity.visitor.Visitor;
import vms.vevs.service.UserService;
import vms.vevs.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {


    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;


    @Autowired
    JWTUtility jwtUtility;

    /*
    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public JWTResponse createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails =   userServiceImpl.loadUserByUsername(authenticationRequest.getUsername());

        final String jwtToken = jwtUtility.generateToken(userDetails);

        return  new JWTResponse(jwtToken);
    }

*/

    @GetMapping(value = "all")
    public HttpResponse<?> listAllUsers() {
        HttpResponse<List<User>> response = new HttpResponse<>();
        response.setResponseObject(userService.findAllUsers());
        return response;
    }


    @GetMapping(value = "view/{id}")
    public HttpResponse<?> getUser(@PathVariable("id") long id) {
        logger.info("Fetching User with id {}", id);
        HttpResponse<User> response = new HttpResponse<>();

        response.setResponseObject(userService.findById(id));
        return response;
    }

    @PostMapping(value = "create")
    public HttpResponse<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", user);
        HttpResponse<User> response = new HttpResponse<>();


        response.setResponseObject(userService.saveUser(user));
        return response;
    }


    @PutMapping(value = "update/{id}")
    public HttpResponse<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        logger.info("Updating User with id {}", id);
        HttpResponse<User> response = new HttpResponse<>();
        User currentUser = userService.findById(id);


        response.setResponseObject(userService.updateUser(currentUser));
        return response;
    }

}
