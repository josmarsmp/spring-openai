package com.josmi.openaichat.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrthographyDTO {

    @NotEmpty(message = "Prompt cannot be empty, please provide some question or message")
    private String prompt;
}
