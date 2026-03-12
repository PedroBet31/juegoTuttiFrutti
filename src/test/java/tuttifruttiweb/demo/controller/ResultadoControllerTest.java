/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import tuttifruttiweb.demo.juez.JuezFactory;
import tuttifruttiweb.demo.juez.gpt.GroqJuezService;
import tuttifruttiweb.demo.juez.local.LocalJuezService;
import tuttifruttiweb.demo.model.Categoria;
import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.service.*;
import tuttifruttiweb.demo.service.interfaz.*;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoControllerTest {

    private ResultadoController controller;
    private GameService gameService;

    @BeforeEach
    void setup() {

        LocalJuezService localJuezService = new LocalJuezService();
        GroqJuezService groqJuezService = new GroqJuezService(null);

        JuezFactory juezReal = new JuezFactory(localJuezService, groqJuezService);
        IJuezFactory juezFactory = new JuezFactoryAdapter(juezReal);

        IGeneradorLetras generadorLetras = new IGeneradorLetras() {
            @Override
            public void reset() {
            }

            @Override
            public char siguienteLetra() {
                return 'A';
            }
        };

        ITimeoutManager timeoutManager = new ITimeoutManager() {
            @Override
            public ScheduledFuture<?> schedule(Runnable task, int seconds) {
                return null;
            }

            @Override
            public void cancel(ScheduledFuture<?> future) {
            }
        };

        Scorer scorer = new Scorer();

        IRondaFactory rondaFactory = (letra, categorias, jugadores) ->
                new tuttifruttiweb.demo.model.Ronda(letra, categorias);

        RoundProcessorService roundProcessor =
                new RoundProcessorService(juezReal, scorer);

        gameService = new GameService(
                juezFactory,
                generadorLetras,
                timeoutManager,
                scorer,
                rondaFactory,
                roundProcessor
        );

        controller = new ResultadoController(gameService);
    }

    @Test
    void resultado_sinPartida_redirigeAlLobby() {

        Model model = new BindingAwareModelMap();

        String view = controller.resultado(model);

        assertEquals("redirect:/lobby", view);
    }

    @Test
    void resultado_conPartidaYRonda_devuelveResultado() {

        gameService.crearPartidaSimple(
                "Pedro",
                List.of(new Categoria("Nombre")),
                60,
                10,
                5
        );

        gameService.iniciarRonda();

        Model model = new BindingAwareModelMap();

        String view = controller.resultado(model);

        assertEquals("resultado", view);
        assertTrue(model.containsAttribute("jugador"));
        assertTrue(model.containsAttribute("ronda"));
        assertTrue(model.containsAttribute("totalRondas"));
    }

    @Test
    void siguiente_sinPartida_redirigeAlLobby() {

        String view = controller.siguiente();

        assertEquals("redirect:/lobby", view);
    }

    @Test
    void resultadoFinal_sinPartida_redirigeAlLobby() {

        Model model = new BindingAwareModelMap();

        String view = controller.resultadoFinal(model);

        assertEquals("redirect:/lobby", view);
    }
}
