/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeneradorLetrasServiceTest {

    @Test
    void generaLetraEntreAyz() {
        GeneradorLetrasService g = new GeneradorLetrasService();
        char c = g.siguienteLetra();
        assertTrue(c >= 'A' && c <= 'Z');
    }

    @Test
    void noRepiteLetrasHastaAgotarlas() {
        GeneradorLetrasService g = new GeneradorLetrasService();

        for (int i = 0; i < 26; i++) {
            char c = g.siguienteLetra();
            assertTrue(c >= 'A' && c <= 'Z');
        }

        assertThrows(IllegalStateException.class, g::siguienteLetra);
    }

    @Test
    void resetPermiteReutilizarLetras() {
        GeneradorLetrasService g = new GeneradorLetrasService();
        g.siguienteLetra();
        g.reset();
        assertDoesNotThrow(g::siguienteLetra);
    }
}
