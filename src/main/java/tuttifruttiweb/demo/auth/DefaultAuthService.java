/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementación concreta del servicio de autenticación.
 * Valida el registro, encripta la contraseña y guarda el usuario.
 * Acá vive la lógica “real” del alta.
 */
@Service
public class DefaultAuthService implements AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final UserValidator validator;

    public DefaultAuthService(UserRepository repo,
                              PasswordEncoder encoder,
                              UserValidator validator) {
        this.repo = repo;
        this.encoder = encoder;
        this.validator = validator;
    }

    @Override
    public boolean register(String username, String email, String rawPassword) {

        if (!validator.isValidUsername(username) ||
            !validator.isValidEmail(email) ||
            !validator.isValidPassword(rawPassword)) {
            return false;
        }

        String u = username.trim();
        if (repo.findByUsernameIgnoreCase(u).isPresent()) return false;

        String encoded = encoder.encode(rawPassword);
        User user = UserFactory.create(u, email.trim(), encoded);

        repo.save(user);
        return true;
    }
}
