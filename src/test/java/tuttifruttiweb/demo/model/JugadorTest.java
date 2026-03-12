/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void constructorAndSumarPuntos_andGetters() {
        Jugador j = new Jugador("Pedro");
        assertEquals("Pedro", j.getNombre());
        assertEquals(0, j.getPuntaje());

        j.sumarPuntos(15);
        assertEquals(15, j.getPuntaje());

        j.sumarPuntos(-5);
        assertEquals(10, j.getPuntaje());
    }

    @Test
    void setNombre_nullOrBlank_throws() {
        Jugador j = new Jugador();
        assertThrows(IllegalArgumentException.class, () -> j.setNombre(null));
        assertThrows(IllegalArgumentException.class, () -> j.setNombre("   "));
    }

    @Test
    void equalsAndHashCode_caseInsensitive() {
        Jugador a = new Jugador("juan");
        Jugador b = new Jugador("JUAN");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void setPuntaje_changesValue() {
        Jugador j = new Jugador("X");
        j.setPuntaje(42);
        assertEquals(42, j.getPuntaje());
    }
}
