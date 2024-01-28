package com.josmi.openaichat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIRequest {
    private List<OpenAIMessage> messages;
    private String model;
    private float temperature;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    @JsonProperty("max_tokens")
    private int maxTokens;


}
