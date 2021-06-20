package vms.vevs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vms.vevs.common.util.VmsUtils;
import vms.vevs.entity.employee.AppUser;
import vms.vevs.repo.UserRepository;
import vms.vevs.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService  {

    @Autowired
    UserRepository userRepository;



    @Override
    public AppUser findById(long id) {
        return userRepository.getById(id);

    }

    @Override
    public AppUser findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public AppUser saveUser(AppUser user) {

        user.setEnable(true);
        user.setCreatedBy("loggedIn user");
        user.setCreatedOn(VmsUtils.currentTime());
        return userRepository.save(user);
    }

    @Override
    public AppUser updateUser(AppUser user) {

       AppUser dbUser= userRepository.getById(user.getId());

        dbUser.setModifiedOn(VmsUtils.currentTime());

       return userRepository.save(dbUser);
    }


    @Override
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public boolean isUserExist(AppUser user) {
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
