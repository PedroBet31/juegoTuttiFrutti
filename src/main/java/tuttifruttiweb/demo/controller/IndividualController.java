/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tuttifruttiweb.demo.model.Categoria;
import tuttifruttiweb.demo.model.Ronda;
import tuttifruttiweb.demo.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controlador MVC del modo individual. Maneja la selección de categorías,
 * creación de la partida, navegación del juego y pantallas intermedias.
 */
@Controller
public class IndividualController {

    private static final Logger log = LoggerFactory.getLogger(IndividualController.class);
    public static final String SESSION_CATEGORIAS = "categoriasElegidas";
    public static final String SESSION_CANT_RONDAS = "cantidadRondas";
    private static final int MAX_CATEGORIA_LENGTH = 12;  

    private final GameService game;

    public IndividualController(GameService game) {
        this.game = Objects.requireNonNull(game);
    }

    @GetMapping("/modo-individual")
    public String modoIndividual(Model model) {
        return "individual";
    }

    @PostMapping("/crear-partida")
    public String crearPartida(
            @RequestParam(required = false, name = "categorias") List<String> categoriasSel,
            @RequestParam(required = false, name = "cantidadRondas") Integer cantidadRondas,
            @RequestParam(required = false, name = "tiempoPorRonda") Integer tiempoPorRonda,
            HttpSession session,
            Model model
    ) {
        game.reset();

        if (categoriasSel == null || categoriasSel.isEmpty()) {
            categoriasSel = List.of("Nombre", "Apellido", "Animal");
        }

        List<Categoria> categorias = new ArrayList<>();
        for (String c : categoriasSel) {
            if (c != null && !c.isBlank()) {
                if (c.length() > MAX_CATEGORIA_LENGTH) {
                    model.addAttribute("catLimit", true);
                    model.addAttribute("errorMessage", "Las categorías personalizadas no pueden tener más de 12 caracteres.");
                    return "individual";
                }
                categorias.add(new Categoria(c));
            }
        }

        if (categorias.size() < 1 || categorias.size() > 8) {
            model.addAttribute("catLimit", true);
            model.addAttribute("errorMessage", "Debe seleccionar entre 1 y 8 categorías.");
            return "individual";
        }

        int rounds = (cantidadRondas != null) ? Math.max(1, Math.min(20, cantidadRondas)) : 3;
        int tiempo = (tiempoPorRonda != null) ? Math.max(10, Math.min(300, tiempoPorRonda)) : 60;

        session.setAttribute(SESSION_CATEGORIAS, categorias);
        session.setAttribute(SESSION_CANT_RONDAS, rounds);

        game.crearPartidaSimple("Jugador", categorias, tiempo, 10, 5, rounds);

        try {
            game.iniciarRonda();
        } catch (Exception e) {
            log.error("No se pudo iniciar la primera ronda tras crear la partida: {}", e.getMessage(), e);
            return "redirect:/modo-individual";
        }

        return "redirect:/juego";
    }

    @GetMapping("/juego")
    public String juego(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Categoria> cats = (List<Categoria>) session.getAttribute(SESSION_CATEGORIAS);

        if (cats == null) {
            Ronda rTmp = game.getRondaActiva();
            if (rTmp != null && rTmp.getCategorias() != null && !rTmp.getCategorias().isEmpty()) {
                cats = rTmp.getCategorias();
                log.debug("Categorias no encontradas en sesión — usando categorías desde la ronda activa como fallback.");
            }
        }

        if (cats == null) {
            return "redirect:/modo-individual";
        }

        if (game.getPartida() == null) {
            log.warn("Acceso a /juego sin partida en GameService -> redirigiendo a configuración");
            return "redirect:/modo-individual";
        }

        if (game.getRondaActiva() == null) {
            try {
                game.iniciarRonda();
            } catch (Exception e) {
                log.error("No se pudo iniciar ronda al entrar a /juego: {}", e.getMessage(), e);
                return "redirect:/modo-individual";
            }
        }

        var ronda = game.getRondaActiva();
        model.addAttribute("letra", ronda.getLetra());
        model.addAttribute("categorias", cats);
        model.addAttribute("tiempo", game.getPartida().getTiempoPorRondaSeg());
        model.addAttribute("rondaIndex", game.getPartida().getRondasJugadas());
        model.addAttribute("totalRondas", game.getPartida().getTotalRondas());

        return "juego";
    }
}
