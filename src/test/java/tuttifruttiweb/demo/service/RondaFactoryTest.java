/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.service;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RondaFactoryTest {

    @Test
    void creaRondaConRespuestasPorJugadorYCategoria() {
        RondaFactory factory = new RondaFactory();

        Jugador j1 = new Jugador("Ana");
        Jugador j2 = new Jugador("Luis");

        Categoria c1 = new Categoria("Animal");
        Categoria c2 = new Categoria("Nombre");

        Ronda r = factory.crearRonda('A', List.of(c1, c2), List.of(j1, j2));

        assertEquals(4, r.getRespuestas().size());
    }
}
