package com.josmi.openaichat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josmi.openaichat.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GptService {

    private final WebClient.Builder webClientBuilder;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String openAIUrl;

    public OrthographyResponse orthographyCheck(String prompt) throws JsonProcessingException {
        OpenAIResponse orthographyResponse =  webClientBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build()
                .post()
                .uri(openAIUrl)
                .body(BodyInserters.fromValue(getOpenAIOrthographyRequest(prompt)))
                .retrieve()
                .bodyToMono(OpenAIResponse.class)
                .block();

        assert orthographyResponse != null;
        Choice choice = orthographyResponse.getChoices().get(0);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(choice.getMessage().getContent(), OrthographyResponse.class);
    }

    public OpenAIMessage prosConsDiscusses(String prompt) {
        OpenAIResponse prosConsDiscussesResponse = webClientBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build()
                .post()
                .uri(openAIUrl)
                .body(BodyInserters.fromValue(getProsConsDiscussesRequest(prompt)))
                .retrieve()
                .bodyToMono(OpenAIResponse.class)
                .block();

        assert prosConsDiscussesResponse != null;
        Choice choice = prosConsDiscussesResponse.getChoices().get(0);

        return choice.getMessage();

    }

    private OpenAIRequest getOpenAIOrthographyRequest(String prompt) {

        String orthographySystemMessage = """
                        Te serán proveídos textos en español con posibles errores ortográficos y gramaticales,
                        Las palabras usadas deben de existir en el diccionario de la Real Academia Española
                        Debes de responder en formato JSON,
                        Tu tarea es corregirlos y retornar información soluciones,
                        También debes de dar un porcentaje de acierto por el usuario,
                        Si no hay errores, debes de retornar un mensaje de felicitaciones.
                        Ejemplo de salida:
                        {
                            userScore: number,
                            errors: string[], //['error -> solución'],
                            message: string, //Usa emojis y texto para felicitar al usuario
                        }
                """;

        OpenAIMessage systemMessage = OpenAIMessage.builder()
                .role("system")
                .content(orthographySystemMessage)
                .build();

        OpenAIMessage userMessage = OpenAIMessage.builder()
                .role("user")
                .content(prompt)
                .build();

        ResponseFormat responseFormat = ResponseFormat.builder().type("json_object").build();

        return OpenAIRequest.builder()
                .messages(List.of(systemMessage, userMessage))
                .model("gpt-3.5-turbo-1106")
                .temperature(0.3f)
                .maxTokens(150)
                .responseFormat(responseFormat)
                .build();
    }

    private OpenAIRequest getProsConsDiscussesRequest(String prompt) {
        String prosConsDiscussesMessage = """
                    Se te dará una pregunta y tu tarea es dar una respuesta con pros y contras,
                    La respuesta debe de ser en formato markdown,
                    Los pros y contras deben de estar en una lista
                """;

        OpenAIMessage systemMessage = OpenAIMessage.builder()
                .role("system")
                .content(prosConsDiscussesMessage)
                .build();

        OpenAIMessage userMessage = OpenAIMessage.builder()
                .role("user")
                .content(prompt)
                .build();

        return OpenAIRequest.builder()
                .messages(List.of(systemMessage, userMessage))
                .model("gpt-3.5-turbo")
                .temperature(0.8f)
                .maxTokens(500)
                .build();
    }
}
