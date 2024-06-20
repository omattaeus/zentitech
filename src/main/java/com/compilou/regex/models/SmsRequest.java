package com.compilou.regex.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class SmsRequest {

    @JsonProperty("phoneNumber")
    @NotBlank
    private final String phoneNumber;

    @JsonProperty("message")
    @NotBlank
    private final String message;
}
