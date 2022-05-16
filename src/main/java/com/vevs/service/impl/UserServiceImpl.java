package com.vevs.service.impl;

import com.vevs.common.notification.EmailService;
import com.vevs.common.util.VMSUtils;
import com.vevs.common.util.VmsConstants;
import com.vevs.entity.common.Location;
import com.vevs.entity.common.Role;
import com.vevs.entity.employee.ResetPassword;
import com.vevs.entity.employee.Users;
import com.vevs.entity.virtualObject.IdentityAvailability;
import com.vevs.entity.virtualObject.UpdateUserVO;
import com.vevs.entity.virtualObject.UserVO;
import com.vevs.i18N.MessageByLocaleService;
import com.vevs.repo.LocationRepository;
import com.vevs.repo.ResetPasswordRepository;
import com.vevs.repo.UserRepository;
import com.vevs.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
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

    @Autowired
    EmailService emailService;

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

        Users user = new Users();

        user.setEnable(true);
        user.setName(userVO.getName());
        user.setEmail(userVO.getEmail());
        user.setUsername(userVO.getUsername());
        user.setMobileNo(userVO.getMobileNo());
        user.setDesignation(userVO.getDesignation());
        user.setEmployeeCode(userVO.getEmployeeCode());

        user.setCreatedBy(loggedInUserId);
        user.setCreatedOn(VMSUtils.currentTime());
        user.setRoles(Collections.singleton(userRole));

        user.setPassword(passwordEncoder.encode(userVO.getPassword()));
        user.setBaseLocation(locRepository.getById(userVO.getBaseLocationId()));
        if (null != userVO.getCurrentLocationId()) {
            user.setCurrentLocation(locRepository.getById(userVO.getCurrentLocationId()));
        }
        Users newlyAddedUser = userRepository.save(user);
        notifyHimAboutVEVS(newlyAddedUser,userVO.getPassword());
        return newlyAddedUser;
    }

    private void notifyHimAboutVEVS(Users newlyAddedUser,String password) {
        emailService.sendEmailAboutVEVSOpenAccount(newlyAddedUser,password);
    }

    @Override
    public Users updateUser(UpdateUserVO userTobeUpdate, Long loggedInUserId) {

        Users dbUser = userRepository.getById(userTobeUpdate.getId());


        dbUser.setName(skipBlankVal(dbUser.getName(),userTobeUpdate.getName()));
        dbUser.setMobileNo(skipBlankVal(dbUser.getMobileNo(),userTobeUpdate.getMobileNo()));
        dbUser.setDesignation(skipBlankVal(dbUser.getDesignation(),userTobeUpdate.getDesignation()));
        dbUser.setEmployeeCode(skipBlankVal(dbUser.getEmployeeCode(),userTobeUpdate.getEmployeeCode()));
        Location baseLoc=locRepository.getById(Long.parseLong(skipBlankVal(Long.toString(dbUser.getBaseLocation().getId()),Long.toString(userTobeUpdate.getBaseLocationId()))));
        dbUser.setBaseLocation(baseLoc);
        Location currLoc=locRepository.getById(Long.parseLong(skipBlankVal(Long.toString(dbUser.getBaseLocation().getId()),Long.toString(userTobeUpdate.getBaseLocationId()))));
        dbUser.setCurrentLocation(currLoc);
        dbUser.setEnable(Boolean.parseBoolean(skipBlankVal(String.valueOf(dbUser.isEnable()),String.valueOf(userTobeUpdate.isEnable()))));



        dbUser.setModifiedOn(VMSUtils.currentTime());
        dbUser.setModifiedBy(loggedInUserId);
        return userRepository.save(dbUser);
    }

    private String skipBlankVal(String dbVal, String updateVal) {
        return StringUtils.isEmpty(updateVal)?dbVal:updateVal;
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
        ResetPassword password = new ResetPassword();

        password.setUserId(user.getId());
        password.setUserEmail(user.getEmail());
        password.setToken(generateToken());
        password.setTokenCreationTime(VMSUtils.currentTime());
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
        dbUser.setModifiedOn(VMSUtils.currentTime());
        dbUser.setModifiedBy(loggedInUserId);
        return userRepository.save(dbUser);
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(VmsConstants.ORG_CODE)
                .append(UUID.randomUUID())
                .append(UUID.randomUUID())
                .append(VMSUtils.createOTP()).toString();
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
