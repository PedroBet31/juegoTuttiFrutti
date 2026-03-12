/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RespuestaTest {

    @Test
    void defaultsOnNewRespuesta() {
        Respuesta r = new Respuesta();
        assertEquals("", r.getTexto());
        assertEquals(Respuesta.Veredicto.VACIA, r.getVeredicto());
        assertEquals("", r.getMotivo());
        assertEquals(0, r.getPuntos());
    }

    @Test
    void setTexto_trimsAndNullBecomesEmpty() {
        Respuesta r = new Respuesta();
        r.setTexto("  Hola  ");
        assertEquals("Hola", r.getTexto());

        r.setTexto(null);
        assertEquals("", r.getTexto());
    }

    @Test
    void setVeredicto_null_defaultsToVACIA() {
        Respuesta r = new Respuesta();
        r.setVeredicto(null);
        assertEquals(Respuesta.Veredicto.VACIA, r.getVeredicto());
    }

    @Test
    void puntos_setAndGet() {
        Respuesta r = new Respuesta();
        r.setPuntos(7);
        assertEquals(7, r.getPuntos());
    }

    @Test
    void toString_containsFields() {
        Jugador j = new Jugador("Ana");
        Categoria c = new Categoria("Nombre");
        Respuesta r = new Respuesta(j, c);
        r.setTexto("Algo");
        r.setPuntos(5);
        String s = r.toString();
        assertTrue(s.contains("Ana"));
        assertTrue(s.contains("Nombre"));
        assertTrue(s.contains("Algo"));
        assertTrue(s.contains("5"));
    }
}
