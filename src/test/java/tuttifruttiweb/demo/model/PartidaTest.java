/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    @Test
    void defaultConstructor_values() {
        Partida p = new Partida();
        assertEquals(30, p.getTiempoPorRondaSeg());
        assertEquals(10, p.getPtsValidaUnica());
        assertEquals(5, p.getPtsValidaDuplicada());
        assertEquals(1, p.getTotalRondas());
    }

    @Test
    void agregarJugador_null_throws() {
        Partida p = new Partida();
        assertThrows(IllegalArgumentException.class, () -> p.agregarJugador(null));
    }

    @Test
    void agregarRonda_null_throws() {
        Partida p = new Partida();
        assertThrows(IllegalArgumentException.class, () -> p.agregarRonda(null));
    }

    @Test
    void setTotalRondas_bounds() {
        Partida p = new Partida();
        p.setTotalRondas(0);
        assertEquals(1, p.getTotalRondas());

        p.setTotalRondas(5);
        assertEquals(5, p.getTotalRondas());
    }

    @Test
    void quedanRondas_logic() {
        Partida p = new Partida(10, 1, 1);
        p.setTotalRondas(3);
        assertTrue(p.quedanRondas());

        Ronda r = new Ronda('A', java.util.List.of(new Categoria("N")));
        p.agregarRonda(r);
        p.agregarRonda(new Ronda('B', java.util.List.of(new Categoria("N"))));
        p.agregarRonda(new Ronda('C', java.util.List.of(new Categoria("N"))));
        assertFalse(p.quedanRondas());
    }
}
