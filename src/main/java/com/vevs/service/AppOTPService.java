package com.vevs.service;

import com.vevs.entity.common.AppOTP;

import java.util.List;

public interface AppOTPService {

    String createAndSendOTP(AppOTP otpRequest);

    boolean isValidOTP(String otp,String email,String mobileNumber);

    List<AppOTP> allPendingOTP();
}
