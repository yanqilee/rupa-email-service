package com.exercise.emailservice.service;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailClient emailClient;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testCallingEmailServiceWithInvalidInput_NullToName() {
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

    @Test
    public void testCallingEmailServiceWithInvalidInput_NameTooLong() {
        EmailRequest emailRequest = EmailRequest.builder()
                .from("abc@test.com")
                .fromName("name name name name name name name name name name name name name name name name")
                .to("def@test.com")
                .toName("name")
                .subject("Hi")
                .body("body")
                .build();

        String errorMessage = "from_name field must be between 1 and 30 characters long;";

        EmailResponse actualResponse = emailService.sendEmail(emailRequest);

        EmailResponse expectedResponse = EmailResponse.builder()
                .statusCode(400)
                .errorMessage(errorMessage)
                .build();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testCallingEmailServiceWithValidInput() {
        EmailRequest emailRequest = EmailRequest.builder()
                .from("abc@test.com")
                .fromName("name")
                .to("def@test.com")
                .toName("name2")
                .subject("Hi")
                .body("body")
                .build();

        EmailResponse expectedResponse = EmailResponse.builder()
                .statusCode(200)
                .build();
        when(emailClient.sendEmail(emailRequest)).thenReturn(expectedResponse);
        EmailResponse actualResponse = emailService.sendEmail(emailRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testCallingEmailServiceWithDangerousHtml() {
        EmailRequest emailRequest = EmailRequest.builder()
                .from("abc@test.com")
                .fromName("name")
                .to("def@test.com")
                .toName("name2")
                .subject("Hi")
                .body("body<script>alert('dangerous html')</script>")
                .build();

        EmailResponse expectedResponse = EmailResponse.builder()
                .statusCode(200)
                .build();
        when(emailClient.sendEmail(emailRequest)).thenReturn(expectedResponse);
        EmailResponse actualResponse = emailService.sendEmail(emailRequest);

        assertEquals("body", emailRequest.getBody());
        assertEquals(expectedResponse, actualResponse);
    }


    @Test
    public void testCallingEmailServiceClientCallFails() {
        EmailRequest emailRequest = EmailRequest.builder()
                .from("abc@test.com")
                .fromName("name")
                .to("def@test.com")
                .toName("name2")
                .subject("Hi")
                .body("body")
                .build();

        EmailResponse expectedResponse = EmailResponse.builder()
                .statusCode(500)
                .errorMessage("Error while calling email client.")
                .build();
        when(emailClient.sendEmail(emailRequest)).thenReturn(expectedResponse);
        EmailResponse actualResponse = emailService.sendEmail(emailRequest);

        assertEquals(expectedResponse, actualResponse);
    }
}