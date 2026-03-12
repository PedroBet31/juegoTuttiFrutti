/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tuttifruttiweb.demo.multiplayer.GameSessionManager;

import java.security.Principal;

/**
 * Controlador de las páginas del modo multijugador. Maneja creación de sala,
 * unión, navegación entre vistas y pantallas relacionadas a cada sala.
 */
@Controller
@RequestMapping("/modo-multijugador")
public class MultiplayerPageController {

    private final GameSessionManager sessionManager;

    public MultiplayerPageController(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @GetMapping("")
    public String modoMultijugador(Model model, Principal principal) {

        if (principal != null)
            model.addAttribute("username", principal.getName());

        return "modo-multijugador";
    }

    @GetMapping("/crear")
    public String crearSala(Model model, Principal principal) {

        if (principal != null)
            model.addAttribute("username", principal.getName());

        return "crear-sala";
    }

    @GetMapping("/unirse")
    public String unirseSala(Model model,
                             Principal principal,
                             @RequestParam(value = "error", required = false) String error) {

        if (principal != null)
            model.addAttribute("username", principal.getName());

        if ("notfound".equals(error)) {
            model.addAttribute("errorMessage", "Código Inexistente");
        }

        return "unirse-sala";
    }

    @GetMapping("/sala/{roomId}")
    public String salaMultiplayer(@PathVariable String roomId,
                                  Model model,
                                  Principal principal) {

        if (sessionManager.get(roomId).isEmpty()) {
            return "redirect:/modo-multijugador/unirse?error=notfound";
        }

        if (principal != null)
            model.addAttribute("username", principal.getName());

        model.addAttribute("roomId", roomId);

        return "sala-multijugador";
    }

    @GetMapping("/game/{roomId}")
    public String game(@PathVariable String roomId,
                       Model model,
                       Principal principal) {

        if (sessionManager.get(roomId).isEmpty()) {
            return "redirect:/modo-multijugador/unirse?error=notfound";
        }

        if (principal != null)
            model.addAttribute("username", principal.getName());

        model.addAttribute("roomId", roomId);

        return "sala-juego";
    }

    @GetMapping("/resultados/{roomId}")
    public String resultadosRonda(@PathVariable String roomId,
                                  Model model,
                                  Principal principal) {

        if (sessionManager.get(roomId).isEmpty()) {
            return "redirect:/modo-multijugador/unirse?error=notfound";
        }

        if (principal != null)
            model.addAttribute("username", principal.getName());

        model.addAttribute("roomId", roomId);
        return "sala-resultados";
    }

    @GetMapping("/final/{roomId}")
    public String resultadoFinal(@PathVariable String roomId,
                                 Model model,
                                 Principal principal) {

        if (sessionManager.get(roomId).isEmpty()) {
            return "redirect:/modo-multijugador/unirse?error=notfound";
        }

        if (principal != null)
            model.addAttribute("username", principal.getName());

        model.addAttribute("roomId", roomId);

        return "resultado-final-multijugador";
    }

}
