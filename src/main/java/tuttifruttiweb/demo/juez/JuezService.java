/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez;

import org.springframework.stereotype.Service;
import tuttifruttiweb.demo.model.Respuesta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Implementación local del juez, basada en un diccionario estático y validaciones básicas.
@Service
public class JuezService implements IJuez {

    private final Set<String> diccionario;

    public JuezService() {
        diccionario = new HashSet<>();
        diccionario.add("gato"); diccionario.add("guatemala"); diccionario.add("gris");
        diccionario.add("mono"); diccionario.add("mexico"); diccionario.add("morado");
    }

    public void addWord(String w) {
        if (w != null && !w.isBlank()) diccionario.add(w.trim().toLowerCase());
    }

    @Override
    public void evaluarRespuestas(List<Respuesta> respuestas, char letra) {
        char upper = Character.toUpperCase(letra);
        for (Respuesta r : respuestas) {
            String txt = r.getTexto();
            if (txt == null || txt.isBlank()) {
                r.setVeredicto(Respuesta.Veredicto.VACIA);
                r.setMotivo("VACÍA");
                continue;
            }
            String t = txt.trim();
            if (Character.toUpperCase(t.charAt(0)) != upper) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("No comienza con " + upper);
                continue;
            }
            if (!diccionario.contains(t.toLowerCase())) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("No reconocida localmente");
                continue;
            }
            r.setVeredicto(Respuesta.Veredicto.VALIDA);
            r.setMotivo("Válida (local)");
        }

        Set<String> vistos = new HashSet<>();
        Set<String> duplicados = new HashSet<>();
        for (Respuesta r : respuestas) {
            if (r.getVeredicto() == Respuesta.Veredicto.VALIDA) {
                String key = r.getTexto().trim().toLowerCase();
                if (vistos.contains(key)) duplicados.add(key);
                else vistos.add(key);
            }
        }
        for (Respuesta r : respuestas) {
            if (r.getVeredicto() == Respuesta.Veredicto.VALIDA) {
                String key = r.getTexto().trim().toLowerCase();
                if (duplicados.contains(key)) {
                    r.setVeredicto(Respuesta.Veredicto.VALIDA_DUPLICADA);
                    r.setMotivo("Válida pero duplicada");
                }
            }
        }
    }
}
