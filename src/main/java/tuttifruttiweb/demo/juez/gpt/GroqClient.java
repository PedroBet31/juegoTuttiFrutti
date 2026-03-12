/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez.gpt;

import com.google.gson.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// Cliente HTTP que se encarga de invocar la API de Groq y devolver su respuesta cruda.
@Component
public class GroqClient {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private final OkHttpClient http = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String apiKey;
    private final String model;

    public GroqClient(
            @Value("${groq.key:}") String apiKey,
            @Value("${groq.model:llama-3.1-8b-instant}") String model
    ) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public String callChatCompletion(String prompt) throws IOException {

        if (apiKey == null || apiKey.isBlank())
            throw new IllegalStateException("Groq API key no configurada.");

        JsonObject root = new JsonObject();
        root.addProperty("model", model);

        JsonArray messages = new JsonArray();
        JsonObject msg = new JsonObject();
        msg.addProperty("role", "user");
        msg.addProperty("content", prompt);
        messages.add(msg);
        root.add("messages", messages);

        RequestBody body = RequestBody.create(
                gson.toJson(root),
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = http.newCall(req).execute()) {

            String respBody = new String(response.body().bytes(), StandardCharsets.UTF_8);

            if (!response.isSuccessful())
                throw new IOException("Groq ERROR " + response.code() + ": " + respBody);

            JsonObject json = gson.fromJson(respBody, JsonObject.class);

            return json
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
}
