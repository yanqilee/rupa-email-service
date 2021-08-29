package com.exercise.emailservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class EmailRequest {

    @NotNull(message = "to field must not be null")
    @Email(message = "to field is not a valid email address")
    private String to;

    @JsonProperty("to_name")
    @NotBlank(message = "to_name field must not be blank")
    @Size(min = 1, max = 30, message = "to_name field must be between 1 to 30 characters long")
    private String toName;

    @NotNull(message = "from field must not be null")
    @Email(message = "from field is not a valid email address")
    private String from;

    @JsonProperty("from_name")
    @NotBlank(message = "from_name field must not be blank")
    @Size(min = 1, max = 30, message = "from_name field must be between 1 to 30 characters long")
    private String fromName;

    @NotBlank(message = "subject field must not be blank")
    @Size(min = 1, max = 100, message = "subject field must be between 1 to 100 characters long")
    private String subject;

    @NotBlank(message = "body field must not be blank")
    @Size(min = 1, max = 500, message = "body field must be between 1 to 500 characters long")
    private String body;
}
