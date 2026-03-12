/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service;

// Procesa una ronda completa: actualiza textos, pasa por el juez, aplica puntajes.
// Es el núcleo que unifica la lógica entre individual y multijugador.

import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.juez.IJuez;
import tuttifruttiweb.demo.juez.JuezFactory;
import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.model.Respuesta;
import tuttifruttiweb.demo.model.Ronda;

import java.util.List;

@Component
public class RoundProcessorService {

    private final JuezFactory juezFactory;
    private final Scorer scorer;

    public RoundProcessorService(JuezFactory juezFactory, Scorer scorer) {
        this.juezFactory = juezFactory;
        this.scorer = scorer;
    }

    public Ronda processRound(Ronda ronda,
                              Partida partida,
                              List<Respuesta> respuestasIngresadas,
                              boolean usarGPT) {

        if (ronda == null) throw new IllegalArgumentException("No hay ronda activa");
        if (partida == null) throw new IllegalArgumentException("No hay partida activa");

        if (respuestasIngresadas != null) {

            for (Respuesta rActual : ronda.getRespuestas()) {
                respuestasIngresadas.stream()
                        .filter(in -> in.getJugador().getNombre().equalsIgnoreCase(rActual.getJugador().getNombre()) &&
                                      in.getCategoria().getNombre().equalsIgnoreCase(rActual.getCategoria().getNombre()))
                        .findFirst()
                        .ifPresent(in -> rActual.setTexto(in.getTexto()));
            }
        }

        IJuez juez = juezFactory.getJuez(usarGPT);
        juez.evaluarRespuestas(ronda.getRespuestas(), ronda.getLetra());

        scorer.aplicarPuntajes(ronda, partida);

        return ronda;
    }

    public void processRoundAutomatic(Ronda ronda, Partida partida) {
        try {
            juezFactory.getJuez(true).evaluarRespuestas(ronda.getRespuestas(), ronda.getLetra());
        } catch (Exception e) {
            juezFactory.getJuez(false).evaluarRespuestas(ronda.getRespuestas(), ronda.getLetra());
        }
        scorer.aplicarPuntajes(ronda, partida);
    }
}
