/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez.local;

import org.springframework.stereotype.Service;
import tuttifruttiweb.demo.juez.IJuez;
import tuttifruttiweb.demo.model.Respuesta;

import java.util.*;

// Juez local que combina validación de letra + diccionario estático para decidir la validez.
@Service
public class LocalJuezService implements IJuez {

    private final Set<String> diccionario = new HashSet<>();

    public LocalJuezService() {
        diccionario.add("gato");
        diccionario.add("guatemala");
        diccionario.add("gris");
        diccionario.add("mono");
        diccionario.add("mexico");
        diccionario.add("morado");
    }

    public void addWord(String w) {
        if (w != null && !w.isBlank())
            diccionario.add(w.trim().toLowerCase());
    }

    @Override
    public void evaluarRespuestas(List<Respuesta> respuestas, char letra) {

        LocalValidator.validarLetra(respuestas, letra);

        for (Respuesta r : respuestas) {
            if (r.getVeredicto() == Respuesta.Veredicto.INVALIDA
             || r.getVeredicto() == Respuesta.Veredicto.VACIA)
                continue;

            String t = r.getTexto().trim().toLowerCase();
            if (!diccionario.contains(t)) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("No reconocida localmente");
            } else {
                r.setVeredicto(Respuesta.Veredicto.VALIDA);
                r.setMotivo("Válida (local)");
            }
        }

        Set<String> vistos = new HashSet<>();
        Set<String> duplicados = new HashSet<>();
        for (Respuesta r : respuestas) {
            if (r.getVeredicto() == Respuesta.Veredicto.VALIDA) {
                String key = r.getTexto().trim().toLowerCase();
                if (!vistos.add(key))
                    duplicados.add(key);
            }
        }
        for (Respuesta r : respuestas) {
            if (duplicados.contains(r.getTexto().trim().toLowerCase())) {
                r.setVeredicto(Respuesta.Veredicto.VALIDA_DUPLICADA);
                r.setMotivo("Válida pero duplicada");
            }
        }
    }
}
