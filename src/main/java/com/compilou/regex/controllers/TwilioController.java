package com.compilou.regex.controllers;

import com.compilou.regex.models.SmsRequest;
import com.compilou.regex.services.TwilioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class TwilioController {

    private final TwilioService twilioService;

    @Autowired
    public TwilioController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping("/send")
    public void sendSms(@Valid @RequestBody SmsRequest smsRequest){
        twilioService.sendSms(smsRequest);
    }
}
