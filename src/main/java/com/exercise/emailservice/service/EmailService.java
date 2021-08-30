package com.exercise.emailservice.service;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;
import lombok.extern.log4j.Log4j2;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
@Log4j2
public class EmailService {

    @Autowired
    private EmailClient emailClient;

    /**
     * Validate and sanitize input, then send an email based on provided details.
     *
     * @param emailRequest Request with details needed to send an email from one party to another
     * @return Response with a status code and an error message if an error occurred
     */
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        String errorMessage = validateEmailRequest(emailRequest);
        if (!errorMessage.isBlank()) {
            return EmailResponse.builder()
                    .statusCode(400)
                    .errorMessage(errorMessage)
                    .build();
        }

        try {
            emailRequest.setBody(sanitizeHtml(emailRequest.getBody()));
        } catch (Exception e) {
            log.error("Error while sanitizing input HTML: " + e.getMessage(), e);
            return EmailResponse.builder()
                    .statusCode(500)
                    .errorMessage("Internal service error, please try again later.")
                    .build();
        }

        return emailClient.sendEmail(emailRequest);
    }

    /**
     * Validate email API input based on rules set with validation annotations.
     *
     * @param emailRequest Request with details needed to send an email from one party to another
     * @return Error message from invalid inputs, empty if input is valid
     */
    private String validateEmailRequest(EmailRequest emailRequest) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EmailRequest>> violations = validator.validate(emailRequest);

        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<EmailRequest> violation : violations) {
            errorMessage.append(violation.getMessage()).append(";");
        }

        return errorMessage.toString();
    }

    private String sanitizeHtml(String untrustedHTML) {
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        return policy.sanitize(untrustedHTML);
    }
}
