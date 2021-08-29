package com.exercise.emailservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailResponse {

    private int statusCode;
    private String errorMessage;
}
