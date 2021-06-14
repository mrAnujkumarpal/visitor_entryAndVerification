package vms.vevs.service;

import org.springframework.security.core.userdetails.UserDetails;
import vms.vevs.entity.employee.User;

import java.util.List;

public interface UserService {

    User findById(long id);

    User findByUserName(String name);

    User saveUser(User user);

    void updateUser(User user);


    List<User> findAllUsers();



    boolean isUserExist(User user);
}
