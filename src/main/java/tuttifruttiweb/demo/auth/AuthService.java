/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tuttifruttiweb.demo.auth;

/**
 * Interfaz del servicio de autenticación.
 * La usamos para desacoplar la lógica de registro del controlador
 * y así poder cambiar la implementación sin romper nada.
 */
public interface AuthService {
    boolean register(String username, String email, String rawPassword);
}
