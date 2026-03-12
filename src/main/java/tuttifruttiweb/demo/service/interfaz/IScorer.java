/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service.interfaz;

// Interfaz responsable de asignar puntos a una ronda.

import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.model.Ronda;

public interface IScorer {
    void aplicarPuntajes(Ronda r, Partida partida);
}
