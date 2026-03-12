/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import tuttifruttiweb.demo.service.GameService;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class LobbyControllerTest {

    static class FakeGameService extends GameService {

        boolean resetCalled = false;

        private FakeGameService() {
            super(null, null, null, null, null, null);
        }

        @Override
        public void reset() {
            resetCalled = true;
        }
    }

    @Test
    void lobby_reseteaJuego_limpiaSesion_y_devuelveVistaLobby() throws Exception {

        LobbyController controller = new LobbyController(null);

        FakeGameService fakeGame = (FakeGameService)
                UnsafeAllocator.allocate(FakeGameService.class);

        Field gameField = null;
        for (Field f : LobbyController.class.getDeclaredFields()) {
            if (f.getType() == GameService.class) {
                gameField = f;
                break;
            }
        }

        assertNotNull(gameField, "No se encontró un campo GameService en LobbyController");

        gameField.setAccessible(true);
        gameField.set(controller, fakeGame);

        HttpSession session = new MockHttpSession();
        session.setAttribute(IndividualController.SESSION_CATEGORIAS, "algo");
        session.setAttribute(IndividualController.SESSION_CANT_RONDAS, 5);

        String view = controller.lobby(session);

        assertEquals("lobby", view);
        assertTrue(fakeGame.resetCalled);
        assertNull(session.getAttribute(IndividualController.SESSION_CATEGORIAS));
        assertNull(session.getAttribute(IndividualController.SESSION_CANT_RONDAS));
    }

    static class UnsafeAllocator {
        static Object allocate(Class<?> cls) throws Exception {
            var unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
            return unsafe.allocateInstance(cls);
        }
    }
}
