/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// Evento WebSocket que anuncia que un jugador se unió a la sala.

public class PlayerJoined {
    private String jugador;
    public PlayerJoined() {}
    public PlayerJoined(String jugador) { this.jugador = jugador; }
    public String getJugador() { return jugador; }
    public void setJugador(String jugador) { this.jugador = jugador; }
}
