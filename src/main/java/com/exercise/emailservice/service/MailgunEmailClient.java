package com.exercise.emailservice.service;

import com.exercise.emailservice.model.EmailRequest;
import com.exercise.emailservice.model.EmailResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
@ConditionalOnProperty(name = "email_service", havingValue = "mailgun")
public class MailgunEmailClient implements EmailClient {

    @Value("${mailgun_url}")
    private String mailgunUrl;

    @Value("${mailgun_api_key}")
    private String mailgunApiKey;

    @Override
    public EmailResponse sendEmail(EmailRequest emailRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth("api", mailgunApiKey);

            MultiValueMap<String, String> emailMap = buildMailgunFormMap(emailRequest);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(emailMap, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(mailgunUrl, request, String.class);

            return EmailResponse.builder()
                    .statusCode(responseEntity.getStatusCodeValue())
                    .build();
        } catch (HttpClientErrorException e) {
            log.error("Error during Mailgun client call: " + e.getMessage(), e);

            return EmailResponse.builder()
                    .statusCode(e.getRawStatusCode())
                    .errorMessage("Error while calling email client.")
                    .build();
        } catch (Exception e) {
            log.error("Error during Mailgun client call: " + e.getMessage(), e);

            return EmailResponse.builder()
                    .statusCode(500)
                    .errorMessage("Internal service failure, please try again later.")
                    .build();
        }
    }

    private MultiValueMap<String, String> buildMailgunFormMap(EmailRequest emailRequest) {
        MultiValueMap<String, String> emailMap = new LinkedMultiValueMap<>();
        emailMap.add("from", formatEmailNameAndAddress(emailRequest.getFromName(), emailRequest.getFrom()));
        emailMap.add("to", formatEmailNameAndAddress(emailRequest.getToName(), emailRequest.getTo()));
        emailMap.add("subject", emailRequest.getSubject());
        emailMap.add("text", emailRequest.getBody());
        return emailMap;
    }

    private String formatEmailNameAndAddress(String name, String address) {
        return String.format("%s <%s>", name, address);
    }
}
