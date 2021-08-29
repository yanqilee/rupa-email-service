package com.exercise.emailservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendgridContent {

    private String type;
    private String value;
}
