package com.exercise.emailservice.service;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailServiceTest {

    private final EmailService emailService = new MailgunEmailService();

    @Test
    public void testCallingEmailServiceWithInvalidInput() {
        EmailRequest emailRequest = EmailRequest.builder()
                .from("abc@test.com")
                .fromName("name")
                .to("def@test.com")
                .subject("Hi")
                .body("body")
                .build();

        String errorMessage = "to_name field must not be blank;";

        EmailResponse actualResponse = emailService.sendEmail(emailRequest);

        EmailResponse expectedResponse = EmailResponse.builder()
                .statusCode(400)
                .errorMessage(errorMessage)
                .build();

        assertEquals(expectedResponse, actualResponse);
    }
}