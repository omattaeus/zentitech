package com.compilou.regex.repositories;


import com.compilou.regex.models.SmsRequest;

public interface SmsSender {
    void sendSms(SmsRequest smsRequest);
}
