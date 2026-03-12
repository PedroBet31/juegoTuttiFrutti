/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service;

// Crea una ronda nueva inicializando una respuesta por cada jugador/categoría.

import tuttifruttiweb.demo.service.interfaz.IRondaFactory;
import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.model.*;

import java.util.List;

@Component
public class RondaFactory implements IRondaFactory {

    @Override
    public Ronda crearRonda(char letra, List<Categoria> categorias, List<Jugador> jugadores) {
        Ronda r = new Ronda(letra, categorias);
        if (jugadores != null) {
            for (Jugador j : jugadores) {
                for (Categoria c : categorias) {
                    r.agregarRespuesta(new Respuesta(j, c));
                }
            }
        }
        return r;
    }
}
