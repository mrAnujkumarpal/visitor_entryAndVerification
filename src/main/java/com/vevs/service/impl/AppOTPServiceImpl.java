package com.vevs.service.impl;

import com.vevs.entity.vo.OtpVO;
import com.vevs.service.AppOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vevs.common.notification.EmailService;
import com.vevs.common.util.VMSUtils;
import com.vevs.entity.common.AppOTP;
import com.vevs.repo.AppOTPRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class AppOTPServiceImpl implements AppOTPService {

    @Autowired
    AppOTPRepository otpRepository;

    @Autowired
    EmailService emailService;


    public String createAndSendOTP(OtpVO otpRequest) {
        otpRepository.deleteByEmail(otpRequest.getEmail());

        String randomOTP = Integer.toString(VMSUtils.createOTP());
        AppOTP appOTP = new AppOTP();
        appOTP.setOtp(randomOTP);
        appOTP.setEmail(otpRequest.getEmail());
        appOTP.setCreatedOn(VMSUtils.currentTime());
        appOTP.setOptValidTill(VMSUtils.addMinutesInCurrentTime(5));

        try {
            otpRepository.save(appOTP);
            //send OTP by email
            emailService.sendEmailOTP(otpRequest.getEmail(), randomOTP);
            //send OTP by sms

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OTP sent successfully";
    }

    @Override
    public boolean isValidOTP(String otp, String email, String mobileNumber) {
        AppOTP otpFromDB = otpRepository.findByMobileNumber(mobileNumber);
        Boolean bool = false;
        if (otpFromDB == null) {
            otpFromDB = otpRepository.findByEmail(email);
            if (otpFromDB == null) {
                return false;
            }
        }
        Timestamp otpValidTill = otpFromDB.getOptValidTill();
        Timestamp currentTimestamp = VMSUtils.currentTime();

        if ((otpFromDB.getOtp()).equals(otp) && otpValidTill.after(currentTimestamp)) {

            bool = true;
        }
        return bool;
    }

    @Override
    public List<AppOTP> allPendingOTP() {
        return otpRepository.findAll();
    }


}
