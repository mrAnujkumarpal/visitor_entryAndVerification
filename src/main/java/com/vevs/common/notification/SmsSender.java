package com.vevs.common.notification;

public interface SmsSender {
    void sendSms(SmsRequest smsRequest);

    // or maybe void sendSms(String phoneNumber, String message);

}
