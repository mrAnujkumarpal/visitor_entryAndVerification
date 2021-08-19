package com.vevs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vevs.entity.common.AppOTP;

@Repository
public interface AppOTPRepository extends JpaRepository<AppOTP,Long> {
    AppOTP findByMobileNumber(String mobileNumber);
    AppOTP findByEmail(String email);
}
