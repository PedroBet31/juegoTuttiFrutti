/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.service;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.util.Temporizador;

import static org.junit.jupiter.api.Assertions.*;

class TimeoutManagerTest {

    @Test
    void agendaTareaSinError() {
        Temporizador temporizador = new Temporizador();
        TimeoutManager manager = new TimeoutManager(temporizador);

        assertDoesNotThrow(() ->
                manager.schedule(() -> {}, 1)
        );

        temporizador.shutdown();
    }
}
