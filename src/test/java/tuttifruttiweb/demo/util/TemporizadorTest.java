/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemporizadorTest {

    @Test
    void ejecutaTareaSinError() throws InterruptedException {
        Temporizador t = new Temporizador();

        final boolean[] ejecutado = {false};

        t.scheduleTimeout(() -> ejecutado[0] = true, 1);

        Thread.sleep(1200);

        assertTrue(ejecutado[0]);
        t.shutdown();
    }
}
