package com.exercise.emailservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SendgridEmailRequest {

    private List<SendgridPersonalization> personalizations;
    private List<SendgridContent> content;
    private SendgridEmailDetail from;
    @JsonProperty("reply_to")
    private SendgridEmailDetail replyTo;
}
