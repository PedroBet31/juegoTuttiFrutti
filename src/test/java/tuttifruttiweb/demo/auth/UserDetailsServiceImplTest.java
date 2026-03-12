/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository repo;
    private UserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(UserRepository.class);
        service = new UserDetailsServiceImpl(repo);
    }

    @Test
    void testLoadUserByUsernameFound() {
        User user = new User("user", "email@test.com", "pass");
        when(repo.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("user");
        assertEquals("user", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(repo.findByUsernameIgnoreCase("user")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("user"));
    }
}
