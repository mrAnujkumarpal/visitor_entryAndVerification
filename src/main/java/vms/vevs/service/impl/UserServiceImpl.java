package vms.vevs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vms.vevs.common.util.VmsConstants;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.common.Role;
import vms.vevs.entity.employee.ResetPassword;
import vms.vevs.entity.employee.Users;
import vms.vevs.entity.virtualObject.IdentityAvailability;
import vms.vevs.entity.virtualObject.UserVO;
import vms.vevs.i18.MessageByLocaleService;
import vms.vevs.repo.LocationRepository;
import vms.vevs.repo.ResetPasswordRepository;
import vms.vevs.repo.UserRepository;
import vms.vevs.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MessageByLocaleService messageSource;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @Autowired
    LocationRepository locRepository;

    @Override
    public Users findById(long id) {
        return userRepository.getById(id);

    }

    @Override
    public Optional<Users> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public Users saveUser(UserVO userVO, Long loggedInUserId, Role userRole) {

        Users user=new Users();

        user.setEnable(true);
        user.setName(userVO.getName());
        user.setEmail(userVO.getEmail());
        user.setUsername(userVO.getUsername());
        user.setMobileNo(userVO.getMobileNo());
        user.setDesignation(userVO.getDesignation());
        user.setEmployeeCode(userVO.getEmployeeCode());

        user.setCreatedBy(loggedInUserId);
        user.setCreatedOn(VmsUtils.currentTime());
        user.setRoles(Collections.singleton(userRole));

        user.setPassword(passwordEncoder.encode(userVO.getPassword()));
        user.setBaseLocation(locRepository.getById(userVO.getBaseLocationId()));
        user.setCurrentLocation(locRepository.getById(userVO.getCurrentLocationId()));
        return userRepository.save(user);
    }

    @Override
    public Users updateUser(Users userTobeUpdate, Long loggedInUserId) {

        Users dbUser = userRepository.getById(userTobeUpdate.getId());

        dbUser.setModifiedOn(VmsUtils.currentTime());
        dbUser.setModifiedBy(loggedInUserId);
        return userRepository.save(dbUser);
    }

    @Override
    public IdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new IdentityAvailability(isAvailable);
    }

    @Override
    public IdentityAvailability checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new IdentityAvailability(isAvailable);
    }


    @Override
    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }


    @Override
    public boolean isUserExist(Users user) {
        return userRepository.existsById(user.getId());
    }
    @Override
    public List<Users> employeesByLocationId(long locId) {

        return findAllUsers().stream()
                .filter(x -> (x.getBaseLocation().getId()).equals(locId))
                .collect(Collectors.toList());

    }

    @Override
    public ResetPassword forgotPassword(String email) {

            Optional<Users> userOptional = userRepository.findByEmail(email);

            Users user = userOptional.get();
            ResetPassword password=new ResetPassword();

            password.setUserId(user.getId());
            password.setUserEmail(user.getEmail());
            password.setToken(generateToken());
            password.setTokenCreationTime(VmsUtils.currentTime());
           return resetPasswordRepository.save(password);


        }



    @Override
    public ResetPassword resetPassword(ResetPassword resetPassword) {

        String token = resetPassword.getToken();

        ResetPassword resetPasswordFromDB = resetPasswordRepository.findByToken(token);

        Optional<Users> userOptional = userRepository.findByEmail(resetPassword.getUserEmail());

        Users user = userOptional.get();
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        user.setEmail(resetPassword.getUserEmail());
        userRepository.save(user);

        resetPasswordRepository.deleteByUserEmail(user.getEmail());


        resetPassword.setToken(StringUtils.EMPTY);
        resetPassword.setTokenCreationTime(null);
        resetPassword.setPassword(StringUtils.EMPTY);
        resetPassword.setUserEmail(user.getEmail());
        resetPassword.setMessage(messageSource.getMessage("success.user.password.update"));
        return resetPassword;

    }

    @Override
    public Users updateUserProfilePhoto(MultipartFile photo, Long loggedInUserId) throws IOException {
        Users dbUser = userRepository.getById(loggedInUserId);
        dbUser.setProfilePhoto(photo.getBytes());
        dbUser.setModifiedOn(VmsUtils.currentTime());
        dbUser.setModifiedBy(loggedInUserId);
        return userRepository.save(dbUser);
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(VmsConstants.ORG_CODE.toString())
                .append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString())
                .append(VmsUtils.createOTP()).toString();
    }

/*
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //To get user From DM
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + userName);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                new ArrayList<>());
    }

 */
}
