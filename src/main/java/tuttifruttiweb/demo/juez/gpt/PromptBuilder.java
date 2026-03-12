/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez.gpt;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import tuttifruttiweb.demo.model.Respuesta;

import java.util.List;

// Clase responsable de construir el prompt JSON para enviarlo a Groq.
public class PromptBuilder {

    public String build(List<Respuesta> respuestas, char letra) {

        letra = Character.toUpperCase(letra);

        JsonArray arr = new JsonArray();
        for (Respuesta r : respuestas) {
            JsonObject obj = new JsonObject();
            obj.addProperty("categoria", r.getCategoria().getNombre());
            obj.addProperty("palabra", r.getTexto());
            arr.add(obj);
        }

        String jsonEntradas = arr.toString();

        return """
        Eres un juez del juego Tutti Frutti.

        ***REGLA FUNDAMENTAL (OBLIGATORIA)***
        - TODA palabra válida DEBE comenzar EXACTAMENTE con la letra '%LETTER%'.
        - Si comienza por la letra pero NO es correcta, explica que no es una respuesta valida.
        - Para cada categoría buscar diccionarios reales para evitar validaciones incorrectas. 
        - Si NO empieza con esa letra: valida = false.
        - No intentes corregir, sugerir, completar ni asumir palabras.
        - No ignores la letra inicial por ningún motivo.
        - Siempre tiene que tener motivo.
        - Asegurarte que la respuesta pertenece a la categoría.
               
        FORMATO DE RESPUESTA (SOLO JSON):
        {
          "evaluaciones":[
            {
              "palabra":"...",
              "categoria":"...",
              "valida":true/false,
              "motivo":"..."
            }
          ]
        }

        Reglas adicionales:
        - Si la palabra existe y empieza con la letra, puede ser válida, pero hay que verificar que lo es.
        - Motivos siempre breves.
        - Puedes ser levemente humorístico solo en casos válidos.
        - Que la categoría "Nombre" valide nombres humanos, no de cualquier cosa.
               
        Entradas:
        %ENTRADAS%
        """
                .replace("%LETTER%", String.valueOf(letra))
                .replace("%ENTRADAS%", jsonEntradas);
    }
}
