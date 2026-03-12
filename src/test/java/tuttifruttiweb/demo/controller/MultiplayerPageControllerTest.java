/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import tuttifruttiweb.demo.multiplayer.GameSessionManager;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiplayerPageControllerTest {

    private MultiplayerPageController controller;

    @BeforeEach
    void setup() {
        GameSessionManager gameSessionManager = new GameSessionManager();
        controller = new MultiplayerPageController(gameSessionManager);
    }

    @Test
    void crearSala_devuelveVistaCrearSala() {
        Model model = new BindingAwareModelMap();
        Principal principal = () -> "Pedro";

        String view = controller.crearSala(model, principal);

        assertEquals("crear-sala", view);
    }

    @Test
    void unirseSala_devuelveVistaUnirseSala() {
        Model model = new BindingAwareModelMap();
        Principal principal = () -> "Pedro";
        String codigoSala = "ABC123";

        String view = controller.unirseSala(model, principal, codigoSala);

        assertEquals("unirse-sala", view);
    }
}
