package com.exercise.emailservice.controller;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;
import com.exercise.emailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public EmailResponse emailService(@RequestBody EmailRequest emailRequest) {
        return emailService.sendEmail(emailRequest);
    }
}
