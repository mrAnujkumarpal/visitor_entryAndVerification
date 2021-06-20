package vms.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vms.vevs.entity.employee.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUserName(String userName);
}
