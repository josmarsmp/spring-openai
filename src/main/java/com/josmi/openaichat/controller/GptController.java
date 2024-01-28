package com.josmi.openaichat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.josmi.openaichat.dto.OpenAIMessage;
import com.josmi.openaichat.dto.OrthographyDTO;
import com.josmi.openaichat.dto.OrthographyResponse;
import com.josmi.openaichat.dto.ProsConsDiscusserDTO;
import com.josmi.openaichat.exception.HandleValidationErrors;
import com.josmi.openaichat.service.GptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping("orthography-check")
    @HandleValidationErrors
    public OrthographyResponse orthographyCheck(
            @Valid() @RequestBody() OrthographyDTO orthographyDto
    ) throws JsonProcessingException {
        return gptService.orthographyCheck(orthographyDto.getPrompt());
    }

    @PostMapping("pros-cons-discusser")
    @HandleValidationErrors
    public OpenAIMessage prosConsDiscusser(
            @Valid() @RequestBody()ProsConsDiscusserDTO prosConsDiscusserDTO
    ) {
        return gptService.prosConsDiscusses(prosConsDiscusserDTO.getPrompt());
    }

}
