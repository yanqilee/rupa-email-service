package com.exercise.emailservice.service;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;

public interface EmailClient {

    /**
     * Calling an email client to send an email based on emailRequest.
     *
     * @param emailRequest Request with details needed to send an email from one party to another
     * @return Response with a status code and an error message if an error occurred
     */
    EmailResponse sendEmail(EmailRequest emailRequest);
}
