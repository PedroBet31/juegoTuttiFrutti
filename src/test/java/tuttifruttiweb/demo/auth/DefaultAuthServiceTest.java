/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultAuthServiceTest {

    private UserRepository repo;
    private PasswordEncoder encoder;
    private UserValidator validator;
    private DefaultAuthService service;

    @BeforeEach
    void setUp() {
        repo = mock(UserRepository.class);
        encoder = new BCryptPasswordEncoder();
        validator = new UserValidator();
        service = new DefaultAuthService(repo, encoder, validator);
    }

    @Test
    void testRegisterSuccess() {
        when(repo.findByUsernameIgnoreCase("user")).thenReturn(Optional.empty());

        boolean result = service.register("user", "user@email.com", "pass");
        assertTrue(result);
        verify(repo).save(any(User.class));
    }

    @Test
    void testRegisterInvalidUsername() {
        boolean result = service.register("u", "user@email.com", "pass");
        assertFalse(result);
        verify(repo, never()).save(any());
    }

    @Test
    void testRegisterInvalidEmail() {
        boolean result = service.register("user", "invalid-email", "pass");
        assertFalse(result);
        verify(repo, never()).save(any());
    }

    @Test
    void testRegisterInvalidPassword() {
        boolean result = service.register("user", "user@email.com", "123");
        assertFalse(result);
        verify(repo, never()).save(any());
    }

    @Test
    void testRegisterUsernameAlreadyExists() {
        when(repo.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(new User()));
        boolean result = service.register("user", "user@email.com", "pass");
        assertFalse(result);
        verify(repo, never()).save(any());
    }
}

