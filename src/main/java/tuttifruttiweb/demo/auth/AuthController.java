/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * Controlador que maneja todo lo relacionado al login y registro.
 * Básicamente es la puerta de entrada del usuario: muestra formularios,
 * procesa el registro y redirige según corresponda.
 */
@Controller
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password
    ) {

        try {
            boolean ok = service.register(username, email, password);

            if (!ok) {
                // Fallo por validación interna (no DB)
                return "redirect:/register?error";
            }

            return "redirect:/login?success";

        } catch (DataIntegrityViolationException e) {
            // Email o username duplicado ⇒ mensaje elegante
            return "redirect:/register?duplicate";

        } catch (Exception e) {
            // Cualquier otra cosa ⇒ error genérico
            return "redirect:/register?error";
        }
    }
}
