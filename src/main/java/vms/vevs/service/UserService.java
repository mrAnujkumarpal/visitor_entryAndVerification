package vms.vevs.service;

import vms.vevs.entity.common.Role;
import vms.vevs.entity.employee.ResetPassword;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.IdentityAvailability;
import vms.vevs.entity.virtualObject.UserVO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users findById(long id);

    Optional<Users> findByUsername(String name);

    Users saveUser(UserVO user, Long userId, Role userRole);

    Users updateUser(Users user,Long userId);

    IdentityAvailability checkUsernameAvailability(String username);

    IdentityAvailability checkEmailAvailability(String email);

    List<Users> findAllUsers();

    Optional<Users> findByUsernameOrEmail(String username, String email);

    boolean isUserExist(Users user);

    List<Users> employeesByLocationId(long locId);

    ResetPassword forgotPassword(String email);

    ResetPassword resetPassword(ResetPassword resetPassword);
}
