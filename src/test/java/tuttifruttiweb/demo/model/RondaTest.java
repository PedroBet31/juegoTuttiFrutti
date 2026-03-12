/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RondaTest {

    @Test
    void constructor_setsLetterAndCategories() {
        Categoria cat = new Categoria("Animal");
        Ronda r = new Ronda('a', List.of(cat));
        assertEquals('A', r.getLetra());
        assertEquals(1, r.getCategorias().size());
        assertEquals("Animal", r.getCategorias().get(0).getNombre());
    }

    @Test
    void setLetra_invalid_throws() {
        Ronda r = new Ronda();
        assertThrows(IllegalArgumentException.class, () -> r.setLetra('1'));
        assertThrows(IllegalArgumentException.class, () -> r.setLetra('?'));
    }

    @Test
    void agregarAndGetRespuesta_returnsInserted() {
        Jugador j = new Jugador("P");
        Categoria c = new Categoria("Nombre");
        Respuesta resp = new Respuesta(j, c);
        Ronda r = new Ronda('b', List.of(c));
        r.agregarRespuesta(resp);

        Respuesta found = r.getRespuesta(new Jugador("P"), new Categoria("Nombre"));
        assertNotNull(found);
        assertSame(resp, found);
    }

    @Test
    void getRespuesta_notFound_returnsNull() {
        Ronda r = new Ronda('C', List.of(new Categoria("X")));
        Respuesta res = r.getRespuesta(new Jugador("no"), new Categoria("X"));
        assertNull(res);
    }

    @Test
    void setCategorias_andSetRespuestas_nullHandled() {
        Ronda r = new Ronda('D', null);
        assertNotNull(r.getCategorias());

        r.setRespuestas(null);
        assertNotNull(r.getRespuestas());
    }
}
