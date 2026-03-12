/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.controller.dto.ApiErrorResponse;
import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.model.Respuesta;
import tuttifruttiweb.demo.model.Ronda;
import tuttifruttiweb.demo.service.GameService;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    static class FakeGameService extends GameService {

        boolean submitCalled = false;
        boolean crearCalled = false;

        Ronda ronda;
        Partida partida;

        public FakeGameService() {
            super(null, null, null, null, null, null);
        }

        @Override
        public void crearPartidaSimple(String jugador, List categorias, int tiempo, int puntajeMax, int penalizacion) {
            crearCalled = true;
        }

        @Override
        public Ronda iniciarRonda() {
            if (ronda == null) throw new IllegalStateException("no se puede");
            return ronda;
        }

        @Override
        public void submitRespuestas(List<Respuesta> respuestas, boolean usarGPT) {
            submitCalled = true;
        }

        @Override
        public Ronda getRondaActiva() {
            return ronda;
        }

        @Override
        public Partida getPartida() {
            return partida;
        }
    }

    @Test
    void crearPartida_valida() {
        FakeGameService game = new FakeGameService();
        GameController controller = new GameController(game);

        ResponseEntity<String> resp = controller.crearPartida("Pedro");

        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(game.crearCalled);
    }

    @Test
    void crearPartida_invalido() {
        FakeGameService game = new FakeGameService();
        GameController controller = new GameController(game);

        ResponseEntity<String> resp = controller.crearPartida("");

        assertEquals(400, resp.getStatusCodeValue());
        assertFalse(game.crearCalled);
    }

    @Test
    void iniciarRonda_conflict() {
        FakeGameService game = new FakeGameService();
        GameController controller = new GameController(game);

        ResponseEntity<?> resp = controller.iniciarRonda();

        assertEquals(409, resp.getStatusCodeValue());
        assertTrue(resp.getBody() instanceof ApiErrorResponse);
    }

    @Test
    void submitRespuestas_ok() {
        FakeGameService game = new FakeGameService();
        GameController controller = new GameController(game);

        ResponseEntity<String> resp = controller.submitRespuestas(true, List.of());

        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(game.submitCalled);
    }

    @Test
    void submitRespuestas_null() {
        FakeGameService game = new FakeGameService();
        GameController controller = new GameController(game);

        ResponseEntity<String> resp = controller.submitRespuestas(false, null);

        assertEquals(400, resp.getStatusCodeValue());
        assertFalse(game.submitCalled);
    }

    @Test
    void resultadoRonda_noContent() {
        FakeGameService game = new FakeGameService();
        GameController controller = new GameController(game);

        ResponseEntity<?> resp = controller.resultadoRonda();

        assertEquals(204, resp.getStatusCodeValue());
    }
}
