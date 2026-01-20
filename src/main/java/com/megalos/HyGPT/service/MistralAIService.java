package com.megalos.HyGPT.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class MistralAIService {
    // Mistral AI API - récupère ta clé sur https://console.mistral.ai/api-keys
    private static final String API_KEY = ""; // Mistral API Key
    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public CompletableFuture<String> ask(String prompt) {
        // System prompt : les instructions pour le personnage
        String systemPrompt = """
        Tu es un Kweebec, une petite créature adorable et curieuse du monde de Hytale.
        Tu parles de manière enjouée et un peu naïve.
        Tu adores la nature, les champignons et explorer les forêts de la Zone 1.
        Tu as peur des Trorks et tu admires les aventuriers humains.
        Réponds toujours en restant dans ton personnage, en 2-3 phrases maximum.
        Tu peux utiliser des expressions comme "Oh là là !", "Par les champignons !", "Kweek kweek !".
        """;

        String jsonBody = """
        {
            "model": "mistral-small-latest",
            "messages": [
                {"role": "system", "content": "%s"},
                {"role": "user", "content": "%s"}
            ],
            "max_tokens": 150
        }
        """.formatted(
                systemPrompt.replace("\"", "\\\"").replace("\n", "\\n"),
                prompt.replace("\"", "\\\"")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Retourne un CompletableFuture pour ne pas bloquer le serveur
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> parseResponse(response.body()));
    }

    private String parseResponse(String json) {
        // Debug : affiche la réponse brute dans les logs
        System.out.println("[HyGPT] API Response: " + json);
        
        // Vérifie s'il y a une erreur
        if (json.contains("\"error\"")) {
            int msgStart = json.indexOf("\"message\":\"") + 11;
            int msgEnd = json.indexOf("\"", msgStart);
            if (msgStart > 10 && msgEnd > msgStart) {
                return "API Error: " + json.substring(msgStart, msgEnd);
            }
            return "API Error - check logs";
        }
        
        // Cherche le contenu de la réponse de l'assistant
        // Format: "choices":[{"message":{"role":"assistant","content":"..."}}]
        String marker = "\"content\":\"";
        int lastIndex = json.lastIndexOf(marker); // Prend le DERNIER content (celui de l'assistant)
        if (lastIndex == -1) {
            return "Parse error - no content found";
        }
        
        int contentStart = lastIndex + marker.length();
        
        // Trouve la fin du content (guillemet non échappé)
        int contentEnd = contentStart;
        while (contentEnd < json.length()) {
            char c = json.charAt(contentEnd);
            if (c == '"' && json.charAt(contentEnd - 1) != '\\') {
                break;
            }
            contentEnd++;
        }
        
        return json.substring(contentStart, contentEnd)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\t", " ");
    }
}