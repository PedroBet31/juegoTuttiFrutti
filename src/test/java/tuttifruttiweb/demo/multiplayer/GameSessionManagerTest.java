/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.model.Categoria;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionManagerTest {

    @Test
    void createSession_creaYRegistraSesion() {
        GameSessionManager manager = new GameSessionManager();

        GameSession session = manager.createSession(
                "Pedro",
                List.of(new Categoria("Animal")),
                60,
                10,
                5,
                3
        );

        assertNotNull(session.getId());
        assertTrue(manager.get(session.getId()).isPresent());
    }

    @Test
    void remove_eliminaSesion() {
        GameSessionManager manager = new GameSessionManager();

        GameSession session = manager.createSession(
                "Pedro",
                null,
                60,
                10,
                5,
                3
        );

        manager.remove(session.getId());

        assertTrue(manager.get(session.getId()).isEmpty());
    }

    @Test
    void listAll_devuelveSesiones() {
        GameSessionManager manager = new GameSessionManager();

        manager.createSession("A", null, 60, 10, 5, 3);
        manager.createSession("B", null, 60, 10, 5, 3);

        assertEquals(2, manager.listAll().size());
    }
}

