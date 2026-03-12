/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import tuttifruttiweb.demo.model.Ronda;
import tuttifruttiweb.demo.model.Respuesta;
import tuttifruttiweb.demo.service.GameService;
import tuttifruttiweb.demo.controller.dto.RoundResponse;
import tuttifruttiweb.demo.controller.dto.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Controlador REST del modo individual. Expone endpoints para crear partida,
 * iniciar ronda, enviar respuestas y obtener los resultados.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private final GameService game;

    public GameController(GameService game) {
        this.game = Objects.requireNonNull(game, "game service required");
    }

    @PostMapping("/create")
    public ResponseEntity<String> crearPartida(@RequestParam String jugador) {
        if (jugador == null || jugador.isBlank()) {
            return ResponseEntity.badRequest().body("Jugador inválido");
        }
        game.crearPartidaSimple(jugador.trim(), null, 30, 10, 5);
        return ResponseEntity.ok("Partida creada para " + jugador.trim());
    }

    @PostMapping("/round/start")
    public ResponseEntity<?> iniciarRonda() {
        try {
            Ronda r = game.iniciarRonda();
            RoundResponse resp = RoundResponse.fromRonda(r, game.getPartida().getTiempoPorRondaSeg());
            return ResponseEntity.ok(resp);
        } catch (IllegalStateException ex) {
            log.warn("No se puede iniciar ronda: {}", ex.getMessage());
            return ResponseEntity.status(409).body(new ApiErrorResponse("Illegal State", ex.getMessage()));
        }
    }

    @PostMapping("/round/submit")
    public ResponseEntity<String> submitRespuestas(@RequestParam(defaultValue = "true") boolean usarGPT,
                                                   @RequestBody List<Respuesta> respuestas) {
        if (respuestas == null) return ResponseEntity.badRequest().body("Lista de respuestas vacía");

        game.submitRespuestas(respuestas, usarGPT);
        return ResponseEntity.ok("Respuestas recibidas y evaluadas");
    }

    @GetMapping("/round/result")
    public ResponseEntity<?> resultadoRonda() {
        Ronda r = game.getRondaActiva();
        if (r == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(r);
    }
}
