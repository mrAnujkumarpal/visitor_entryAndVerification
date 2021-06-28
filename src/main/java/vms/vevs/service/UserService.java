package vms.vevs.service;

import vms.vevs.entity.employee.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users findById(long id);

    Optional<Users> findByUsername(String name);

    Users saveUser(Users user,Long userId);

    Users updateUser(Users user,Long userId);


    List<Users> findAllUsers();

    Optional<Users> findByUsernameOrEmail(String username, String email);

    boolean isUserExist(Users user);
}
