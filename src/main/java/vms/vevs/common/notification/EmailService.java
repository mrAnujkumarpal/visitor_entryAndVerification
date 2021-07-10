package vms.vevs.common.notification;

import vms.vevs.entity.visitor.Visitor;

public interface EmailService {

   void sendEmailOTP(String to,String otp);

   void sendEmailToHostEmployee(Visitor visitor);
}
