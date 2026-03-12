/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private final UserValidator validator = new UserValidator();

    @Test
    void testValidUsername() {
        assertTrue(validator.isValidUsername("user"));
        assertFalse(validator.isValidUsername("ab"));
        assertFalse(validator.isValidUsername(null));
    }

    @Test
    void testValidEmail() {
        assertTrue(validator.isValidEmail("user@test.com"));
        assertFalse(validator.isValidEmail("invalid-email"));
        assertFalse(validator.isValidEmail(null));
    }

    @Test
    void testValidPassword() {
        assertTrue(validator.isValidPassword("1234"));
        assertFalse(validator.isValidPassword("123"));
        assertFalse(validator.isValidPassword(null));
    }
}
