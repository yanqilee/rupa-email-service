package com.exercise.emailservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SendgridPersonalization {

    private List<SendgridEmailDetail> to;
    private String subject;
}
