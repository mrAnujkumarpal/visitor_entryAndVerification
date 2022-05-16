package com.vevs.common.notification;

import com.vevs.entity.employee.Users;
import com.vevs.entity.visitor.Visitor;

public interface EmailService {

   void sendEmailOTP(String to,String otp);

   void sendEmailToHostEmployee(Visitor visitor);

   void sendEmailAboutVEVSOpenAccount(Users user,String pass);
}
