/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.auth;

/**
 * Factory simple para crear usuarios.
 * Lo usamos para tener un punto único de construcción
 * por si algún día queremos agregar más lógica (roles, flags, etc.).
 */
public class UserFactory {

    public static User create(String username, String email, String encodedPassword) {
        return new User(username, email, encodedPassword);
    }
}
