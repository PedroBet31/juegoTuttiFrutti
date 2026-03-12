/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service.interfaz;

// Interfaz para factoría de rondas. Crea la ronda inicial con sus respuestas.

import tuttifruttiweb.demo.model.*;
import java.util.List;

public interface IRondaFactory {
    Ronda crearRonda(char letra, List<Categoria> categorias, List<Jugador> jugadores);
}

