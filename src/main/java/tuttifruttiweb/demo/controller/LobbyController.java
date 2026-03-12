/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tuttifruttiweb.demo.service.GameService;

/**
 * Controlador del lobby principal. Reinicia el juego y limpia la sesión
 * para garantizar que todo empiece desde cero cada vez que entramos.
 */
@Controller
public class LobbyController {

    private static final Logger log = LoggerFactory.getLogger(LobbyController.class);

    private final GameService game;

    public LobbyController(GameService game) {
        this.game = game;
    }

    @GetMapping("/lobby")
    public String lobby(HttpSession session) {
        game.reset();
        session.removeAttribute(IndividualController.SESSION_CATEGORIAS);
        session.removeAttribute(IndividualController.SESSION_CANT_RONDAS);
        log.info("Lobby: estado de juego reiniciado");
        return "lobby";
    }
}

