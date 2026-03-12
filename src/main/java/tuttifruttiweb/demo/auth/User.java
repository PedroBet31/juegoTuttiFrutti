/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad User. Representa un usuario real guardado en la base.
 * Tiene validaciones básicas y mapea la tabla "usuarios".
 * Es lo que Spring Security termina usando para autenticar.
 */
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3)
    @Column(nullable = false, length = 50)
    private String username;

    @Email
    @NotBlank
    @Column(nullable = false, length = 120)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username.trim();
        this.email = email.trim();
        this.password = password;
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username.trim(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.trim(); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
