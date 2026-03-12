/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.service;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.juez.JuezFactory;
import tuttifruttiweb.demo.juez.gpt.GroqClient;
import tuttifruttiweb.demo.juez.gpt.GroqJuezService;
import tuttifruttiweb.demo.juez.local.LocalJuezService;
import tuttifruttiweb.demo.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RoundProcessorServiceTest {

    @Test
    void procesaRondaConJuezYScorer() {

        GroqClient dummyClient = new GroqClient("fake-api-key", "llama-3.1-8b-instant");
        GroqJuezService groq = new GroqJuezService(dummyClient);
        LocalJuezService local = new LocalJuezService();

        JuezFactory juezFactory = new JuezFactory(local, groq);
        Scorer scorer = new Scorer();

        RoundProcessorService service =
                new RoundProcessorService(juezFactory, scorer);

        Categoria c = new Categoria("Animal");
        Jugador j = new Jugador("Pedro");

        Respuesta r = new Respuesta(j, c);
        r.setTexto("Araña");

        Ronda ronda = new Ronda('A', List.of(c));
        ronda.agregarRespuesta(r);

        Partida partida = new Partida(60, 10, 5);

        assertDoesNotThrow(() ->
                service.processRound(ronda, partida, List.of(r), false)
        );
    }
}
