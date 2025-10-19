package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    @Value("${gemini.api.key}") // Injectează cheia din application.properties
    private String geminiApiKey;

    @Value("${gemini.api.url}") // Injectează URL-ul
    private String geminiApiUrl;

    // Folosim WebClient pentru a face cererea HTTP
    private final WebClient webClient = WebClient.create();

    // Endpoint-ul pe care îl va apela React
    @PostMapping
    public Mono<String> chatWithGemini(@RequestBody String prompt) {
        // 1. Construim corpul cererii (request body) așa cum îl vrea API-ul Gemini
        String requestBody = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}",
                prompt.replace("\"", "\\\"") // Asigurăm formatarea corectă a JSON-ului
        );

        // 2. Facem cererea către API-ul Gemini
        return webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey) // Adăugăm cheia ca parametru URL
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class) // Primim răspunsul ca text
                .map(this::extractTextFromGeminiResponse); // Trimitem la o funcție de "curățare"
    }

    // 3. Funcție utilitară pentru a extrage doar textul din răspunsul JSON complex al Gemini
    private String extractTextFromGeminiResponse(String responseJson) {
        try {
            // Aceasta este o parsare simplă; ideal ar fi să folosești o bibliotecă (Jackson)
            // Căutăm câmpul "text" din răspuns
            int textIndex = responseJson.indexOf("\"text\": \"") + 9;
            int endIndex = responseJson.indexOf("\"", textIndex);
            return responseJson.substring(textIndex, endIndex).replace("\\n", "\n");
        } catch (Exception e) {
            return "Ne pare rău, a apărut o eroare la procesarea răspunsului.";
        }
    }
}