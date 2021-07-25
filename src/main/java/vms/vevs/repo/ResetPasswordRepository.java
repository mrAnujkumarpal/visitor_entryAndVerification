package vms.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vms.vevs.entity.employee.ResetPassword;
import vms.vevs.entity.employee.Users;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {


    ResetPassword findByToken(String token);

    public void deleteByUserEmail(String email);
}
