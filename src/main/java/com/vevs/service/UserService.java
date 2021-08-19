package com.vevs.service;

import org.springframework.web.multipart.MultipartFile;
import com.vevs.entity.common.Role;
import com.vevs.entity.employee.ResetPassword;
import com.vevs.entity.employee.Users;
import com.vevs.entity.vo.IdentityAvailability;
import com.vevs.entity.vo.UserVO;

import java.io.IOException;
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

    Users updateUserProfilePhoto(MultipartFile photo, Long loggedInUserId) throws IOException;
}
