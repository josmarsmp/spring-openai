package com.josmi.openaichat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrthographyResponse {
    private int userScore;
    private List<String> errors;
    private String message;
}
