/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tuttifruttiweb.demo.model.Ronda;
import tuttifruttiweb.demo.service.GameService;

/**
 * Controlador de pantallas de resultados del modo individual.
 * Maneja la pantalla de ronda, transición entre rondas y el final.
 */
@Controller
public class ResultadoController {

    private static final Logger log = LoggerFactory.getLogger(ResultadoController.class);

    private final GameService game;

    public ResultadoController(GameService game) {
        this.game = game;
    }

    @GetMapping("/resultado")
    public String resultado(Model model) {

        if (game.getPartida() == null)
            return "redirect:/lobby";

        Ronda r = game.getRondaActiva();

        if (r == null) {
            return "redirect:/juego";
        }

        model.addAttribute("ronda", r);
        model.addAttribute("jugador", game.getPartida().getJugadores().get(0));
        int index = game.getPartida().getRondasJugadas();
        model.addAttribute("rondaIndex", index);
        model.addAttribute("totalRondas", game.getPartida().getTotalRondas());
        model.addAttribute("quedan", game.getPartida().quedanRondas());

        return "resultado";
    }

    @PostMapping("/resultado/siguiente")
    public String siguiente() {

        if (game.getPartida() == null)
            return "redirect:/lobby";

        if (!game.getPartida().quedanRondas())
            return "redirect:/resultado-final-individual";

        try {
            game.clearRondaActiva();
            game.iniciarRonda();

        } catch (Exception ex) {
            log.error("Error iniciando siguiente ronda: {}", ex.getMessage(), ex);
            return "redirect:/resultado-final-individual"; 
        }


        return "redirect:/juego";
    }
    
    @GetMapping("/resultado-final-individual")
    public String resultadoFinal(Model model) {

        if (game.getPartida() == null) {
            log.warn("Intento de ver resultado final sin partida activa");
            return "resultado-final";
        }

        model.addAttribute("jugador", game.getPartida().getJugadores().get(0));
        model.addAttribute("rondas", game.getPartida().getRondas());
        model.addAttribute("totalRondas", game.getPartida().getTotalRondas());

        return "resultado-final";
    }


}
