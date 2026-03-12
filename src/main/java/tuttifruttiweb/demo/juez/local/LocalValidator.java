/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez.local;

import java.util.List;
import tuttifruttiweb.demo.model.Respuesta;

// Validador auxiliar que chequea letra inicial y caracteres válidos antes de aplicar diccionario.
public class LocalValidator {

    public static void validarLetra(List<Respuesta> respuestas, char letra) {
        char upper = Character.toUpperCase(letra);

        for (Respuesta r : respuestas) {
            String txt = r.getTexto();

            if (txt == null || txt.isBlank()) {
                r.setVeredicto(Respuesta.Veredicto.VACIA);
                r.setMotivo("VACÍA");
                continue;
            }

            String t = txt.trim();

            if (contieneCaracteresInvalidos(t)) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("Solo se permiten letras (sin números ni símbolos)");
                continue;
            }

            if (Character.toUpperCase(t.charAt(0)) != upper) {
                r.setVeredicto(Respuesta.Veredicto.INVALIDA);
                r.setMotivo("No comienza con " + upper);
            }
        }
    }

    private static boolean contieneCaracteresInvalidos(String palabra) {
        return !palabra.matches("^[\\p{L} ]+$");
    }
}
