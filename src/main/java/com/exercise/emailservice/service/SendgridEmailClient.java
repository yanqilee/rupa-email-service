package com.exercise.emailservice.service;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;
import com.exercise.emailservice.model.SendgridContent;
import com.exercise.emailservice.model.SendgridEmailDetail;
import com.exercise.emailservice.model.SendgridEmailRequest;
import com.exercise.emailservice.model.SendgridPersonalization;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Log4j2
@ConditionalOnProperty(name = "email_service", havingValue = "sendgrid")
public class SendgridEmailClient implements EmailClient {

    @Value("${sendgrid_url}")
    private String sendgridUrl;

    @Value("${sendgrid_api_key}")
    private String sendgridApiKey;

    @Override
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sendgridApiKey);

            SendgridEmailRequest sendgridEmailRequest = buildSendgridEmailRequest(emailRequest);

            HttpEntity<SendgridEmailRequest> request = new HttpEntity<>(sendgridEmailRequest, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(sendgridUrl, request, String.class);

            return EmailResponse.builder()
                    .statusCode(responseEntity.getStatusCodeValue())
                    .build();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage(), e);

            return EmailResponse.builder()
                    .statusCode(e.getRawStatusCode())
                    .errorMessage("Error while calling email client.")
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return EmailResponse.builder()
                    .statusCode(500)
                    .errorMessage("Internal service failure, please try again later.")
                    .build();
        }
    }

    private SendgridEmailRequest buildSendgridEmailRequest(EmailRequest emailRequest) {
        SendgridEmailDetail toDetail = SendgridEmailDetail.builder()
                .email(emailRequest.getTo())
                .name(emailRequest.getToName())
                .build();
        SendgridEmailDetail fromDetail = SendgridEmailDetail.builder()
                .email(emailRequest.getFrom())
                .name(emailRequest.getFromName())
                .build();
        SendgridEmailDetail replyToDetail = SendgridEmailDetail.builder()
                .email(emailRequest.getFrom())
                .name(emailRequest.getFromName())
                .build();

        SendgridPersonalization sendgridPersonalization = SendgridPersonalization.builder()
                .subject(emailRequest.getSubject())
                .to(List.of(toDetail))
                .build();

        SendgridContent sendgridContent = SendgridContent.builder()
                .type("text/plain")
                .value(emailRequest.getBody())
                .build();

        return SendgridEmailRequest.builder()
                .personalizations(List.of(sendgridPersonalization))
                .content(List.of(sendgridContent))
                .from(fromDetail)
                .replyTo(replyToDetail)
                .build();
    }
}
