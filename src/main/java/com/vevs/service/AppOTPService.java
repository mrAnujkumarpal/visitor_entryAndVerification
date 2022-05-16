package com.vevs.service;

import com.vevs.entity.common.AppOTP;
import com.vevs.entity.virtualObject.OtpVO;

import java.util.List;

public interface AppOTPService {

    String createAndSendOTP(OtpVO otpRequest);

    boolean isValidOTP(String otp,String email,String mobileNumber);

    List<AppOTP> allPendingOTP();
}
