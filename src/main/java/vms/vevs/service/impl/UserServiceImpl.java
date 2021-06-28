package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.employee.Users;
import vms.vevs.repo.UserRepository;
import vms.vevs.service.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Users findById(long id) {
        return userRepository.getById(id);

    }

    @Override
    public Optional<Users> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public Users saveUser(Users user, Long loggedInUserId) {

        user.setEnable(true);
        user.setCreatedBy(loggedInUserId);
        user.setCreatedOn(VmsUtils.currentTime());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Users updateUser(Users user, Long userId) {

        Users dbUser = userRepository.getById(user.getId());

        dbUser.setModifiedOn(VmsUtils.currentTime());
        dbUser.setModifiedBy(userId);
        return userRepository.save(dbUser);
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
