/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// DTO simple para devolver errores al cliente cuando algo falla en WebSocket.

public class ErrorMessage {
    private String message;
    public ErrorMessage() {}
    public ErrorMessage(String message) { this.message = message; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
