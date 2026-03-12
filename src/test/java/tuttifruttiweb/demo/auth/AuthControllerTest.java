/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        controller = new AuthController(authService);
    }

    @Test
    void testRootRedirectsToLogin() {
        assertEquals("redirect:/login", controller.root());
    }

    @Test
    void testLoginFormReturnsLoginView() {
        assertEquals("login", controller.loginForm());
    }

    @Test
    void testRegisterFormReturnsRegisterView() {
        assertEquals("register", controller.registerForm());
    }

    @Test
    void testRegisterSuccess() {
        when(authService.register("user", "user@email.com", "pass")).thenReturn(true);

        String result = controller.register("user", "user@email.com", "pass");
        assertEquals("redirect:/login?success", result);
    }

    @Test
    void testRegisterValidationFailure() {
        when(authService.register("user", "user@email.com", "pass")).thenReturn(false);

        String result = controller.register("user", "user@email.com", "pass");
        assertEquals("redirect:/register?error", result);
    }

    @Test
    void testRegisterDuplicateUser() {
        when(authService.register("user", "user@email.com", "pass"))
                .thenThrow(DataIntegrityViolationException.class);

        String result = controller.register("user", "user@email.com", "pass");
        assertEquals("redirect:/register?duplicate", result);
    }

    @Test
    void testRegisterOtherException() {
        when(authService.register("user", "user@email.com", "pass"))
                .thenThrow(new RuntimeException("Boom"));

        String result = controller.register("user", "user@email.com", "pass");
        assertEquals("redirect:/register?error", result);
    }
}
