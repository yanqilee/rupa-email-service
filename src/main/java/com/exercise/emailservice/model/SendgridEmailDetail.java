package com.exercise.emailservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendgridEmailDetail {

    private String email;
    private String name;
}
