/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez.gpt;

import com.google.gson.*;
import tuttifruttiweb.demo.model.Respuesta;
import java.util.List;

// Parser que interpreta la respuesta JSON enviada por Groq y la aplica a las respuestas del juego.
public class GPTJsonParser {

    public void applyGPTResponse(String raw, List<Respuesta> respuestas) {

        JsonObject json;
        try {
            json = JsonParser.parseString(raw).getAsJsonObject();
        } catch (Exception e) {
            marcarError(respuestas, "JSON inválido de Groq");
            return;
        }

        JsonArray arr = json.getAsJsonArray("evaluaciones");
        if (arr == null) {
            marcarError(respuestas, "Groq no devolvió 'evaluaciones'");
            return;
        }

        for (JsonElement e : arr) {

            if (!e.isJsonObject()) continue;
            JsonObject ev = e.getAsJsonObject();

            String palabra = optString(ev, "palabra");
            String categoria = optString(ev, "categoria");
            Boolean valida = optBool(ev, "valida");
            String motivo = optString(ev, "motivo");

            for (Respuesta r : respuestas) {
                if (r.getTexto().equalsIgnoreCase(palabra)
                        && r.getCategoria().getNombre().equalsIgnoreCase(categoria)) {

                    if (valida == null) {
                        r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                        r.setMotivo("Groq no devolvió campo 'valida'.");
                        continue;
                    }

                    r.setVeredicto(valida ?
                            Respuesta.Veredicto.VALIDA :
                            Respuesta.Veredicto.INVALIDA);

                    r.setMotivo(motivo != null ? motivo : "Sin motivo");
                }
            }
        }
    }

    private String optString(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsString()
                : "";
    }

    private Boolean optBool(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsBoolean()
                : null;
    }

    private void marcarError(List<Respuesta> respuestas, String motivo) {
        for (Respuesta r : respuestas) {
            r.setVeredicto(Respuesta.Veredicto.INVALIDA);
            r.setMotivo(motivo);
        }
    }
}
