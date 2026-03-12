/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;
import tuttifruttiweb.demo.model.Categoria;
import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.model.Ronda;
import tuttifruttiweb.demo.service.GameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndividualControllerTest {

    static class FakeGameService extends GameService {

        boolean resetCalled = false;
        boolean crearCalled = false;
        boolean iniciarCalled = false;

        Ronda ronda = new Ronda('A', List.of(new Categoria("Nombre")));
        Partida partida = new Partida(30, 10, 5);

        public FakeGameService() {
            super(null, null, null, null, null, null);
        }

        @Override
        public void reset() {
            resetCalled = true;
        }

        @Override
        public void crearPartidaSimple(String jugador, List categorias, int tiempo, int puntajeMax, int penalizacion, int totalRondas) {
            crearCalled = true;
        }

        @Override
        public Ronda iniciarRonda() {
            iniciarCalled = true;
            return ronda;
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
    void modoIndividual_devuelveVista() {
        FakeGameService game = new FakeGameService();
        IndividualController controller = new IndividualController(game);

        String view = controller.modoIndividual(new ExtendedModelMap());

        assertEquals("individual", view);
    }

    @Test
    void crearPartida_ok() {
        FakeGameService game = new FakeGameService();
        IndividualController controller = new IndividualController(game);

        HttpSession session = new MockHttpSession();
        Model model = new ExtendedModelMap();

        String view = controller.crearPartida(
                List.of("Nombre", "Animal"),
                3,
                60,
                session,
                model
        );

        assertTrue(game.resetCalled);
        assertTrue(game.crearCalled);
        assertTrue(game.iniciarCalled);
        assertEquals("redirect:/juego", view);
    }

    @Test
    void crearPartida_categoriasInvalidas() {
        FakeGameService game = new FakeGameService();
        IndividualController controller = new IndividualController(game);

        HttpSession session = new MockHttpSession();
        Model model = new ExtendedModelMap();

        List<String> categorias = List.of("1","2","3","4","5","6","7","8","9");

        String view = controller.crearPartida(
                categorias,
                3,
                60,
                session,
                model
        );

        assertEquals("individual", view);
        assertFalse(game.crearCalled);
    }

    @Test
    void juego_sinSesion_peroConFallback_devuelveJuego() {
        FakeGameService game = new FakeGameService(); // tiene ronda activa con categorias
        IndividualController controller = new IndividualController(game);

        HttpSession session = new MockHttpSession();
        Model model = new ExtendedModelMap();

        String view = controller.juego(session, model);

        assertEquals("juego", view);
    }


    @Test
    void juego_ok() {
        FakeGameService game = new FakeGameService();
        IndividualController controller = new IndividualController(game);

        HttpSession session = new MockHttpSession();
        session.setAttribute(IndividualController.SESSION_CATEGORIAS,
                List.of(new Categoria("Nombre")));

        Model model = new ExtendedModelMap();

        String view = controller.juego(session, model);

        assertEquals("juego", view);
        assertNotNull(model.getAttribute("letra"));
        assertNotNull(model.getAttribute("categorias"));
    }
}
