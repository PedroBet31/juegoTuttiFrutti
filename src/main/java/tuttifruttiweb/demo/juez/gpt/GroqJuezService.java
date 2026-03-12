/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez.gpt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tuttifruttiweb.demo.juez.IJuez;
import tuttifruttiweb.demo.model.Respuesta;

import java.text.Normalizer;
import java.util.*;

// Juez basado en Groq: valida letra localmente y delega validación semántica a la IA.
@Service
public class GroqJuezService implements IJuez {

    private static final Logger log = LoggerFactory.getLogger(GroqJuezService.class);
    private final GroqClient client;
    private final PromptBuilder promptBuilder = new PromptBuilder();
    private final GPTJsonParser parser = new GPTJsonParser();

    public GroqJuezService(GroqClient client) {
        this.client = client;
    }

    @Override
    public void evaluarRespuestas(List<Respuesta> respuestas, char letra) {

        validarLocal(respuestas, letra);

        try {
            String prompt = promptBuilder.build(respuestas, letra);
            String raw = client.callChatCompletion(prompt);

            if (raw == null || raw.isBlank()) {
                throw new IllegalStateException("Groq devolvió respuesta vacía");
            }

            String jsonLimpio = extraerJson(raw);

            parser.applyGPTResponse(jsonLimpio, respuestas);

        } catch (Exception e) {
            log.warn("Error Groq (individual): {}", e.getMessage());

            marcarTodas(respuestas, "No se pudo validar automáticamente");
        }

        aplicarDuplicados(respuestas);
    }


    private void validarLocal(List<Respuesta> respuestas, char letra) {
        for (Respuesta r : respuestas) {
            String palabra = r.getTexto() == null ? "" : r.getTexto().trim();

            if (palabra.isEmpty()) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("Vacía");
                continue;
            }
            if (contieneCaracteresInvalidos(palabra)) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("Solo se permiten letras (sin números ni símbolos)");
                continue;
            }

            String normalizada = Normalizer.normalize(palabra, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "");

            if (Character.toUpperCase(normalizada.charAt(0)) != Character.toUpperCase(letra)) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("No comienza con '" + letra + "'");
            }
        }
    }

    private void marcarTodas(List<Respuesta> res, String motivo) {
        for (Respuesta r : res) {
            r.setVeredicto(Respuesta.Veredicto.INVALIDA);
            r.setMotivo(motivo);
        }
    }

    private void aplicarDuplicados(List<Respuesta> respuestas) {

        Map<String, Integer> conteo = new HashMap<>();

        for (Respuesta r : respuestas) {
            if (r.getVeredicto() == Respuesta.Veredicto.VALIDA) {
                String k = r.getTexto().trim().toLowerCase();
                conteo.put(k, conteo.getOrDefault(k, 0) + 1);
            }
        }

        for (Respuesta r : respuestas) {
            if (r.getVeredicto() == Respuesta.Veredicto.VALIDA) {
                String k = r.getTexto().trim().toLowerCase();
                if (conteo.get(k) > 1) {
                    r.setVeredicto(Respuesta.Veredicto.VALIDA_DUPLICADA);
                    r.setMotivo("Válida pero duplicada");
                }
            }
        }
    }

    private boolean contieneCaracteresInvalidos(String palabra) {
        return !palabra.matches("^[\\p{L}]+( [\\p{L}]+)*$");
    }

    private String extraerJson(String raw) {

        int inicio = raw.indexOf("{");
        int fin = raw.lastIndexOf("}");

        if (inicio == -1 || fin == -1 || fin <= inicio) {
            throw new IllegalStateException("Groq no devolvió JSON válido");
        }

        String json = raw.substring(inicio, fin + 1);

        // Defensa extra: JSON mínimo
        if (!json.contains(":")) {
            throw new IllegalStateException("JSON incompleto");
        }

        return json;
    }


}
