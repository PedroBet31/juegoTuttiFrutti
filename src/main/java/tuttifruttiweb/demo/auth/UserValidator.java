/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.springframework.stereotype.Component;

/**
 * Validador de campos del registro.
 * Verifica username, email y contraseña antes de crear un usuario.
 */
@Component
public class UserValidator {

    public boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3;
    }

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        String e = email.trim();
        return e.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 4;
    }
}
