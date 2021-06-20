package vms.vevs.service;

import vms.vevs.entity.employee.AppUser;

import java.util.List;

public interface UserService {

    AppUser findById(long id);

    AppUser findByUserName(String name);

    AppUser saveUser(AppUser user);

    AppUser updateUser(AppUser user);


    List<AppUser> findAllUsers();



    boolean isUserExist(AppUser user);
}
