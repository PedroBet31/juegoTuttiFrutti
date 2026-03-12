/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service;

// Asigna puntos a cada respuesta según el veredicto, sin tocar puntajes del jugador.

import tuttifruttiweb.demo.service.interfaz.IScorer;
import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.model.Respuesta;
import tuttifruttiweb.demo.model.Ronda;

@Component
public class Scorer implements IScorer {

    @Override
    public void aplicarPuntajes(Ronda r, Partida partida) {
        if (r == null || partida == null) return;

        for (Respuesta resp : r.getRespuestas()) {
            int pts = switch (resp.getVeredicto()) {
                case VALIDA -> partida.getPtsValidaUnica();
                case VALIDA_DUPLICADA -> partida.getPtsValidaDuplicada();
                default -> 0;
            };

            resp.setPuntos(pts);
            
            resp.getJugador().sumarPuntos(pts);
        }
    }
}


