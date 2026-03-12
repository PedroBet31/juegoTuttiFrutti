/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// DTO para encapsular el pedido de unirse a una sala. El controller
// en realidad ya no lo usa siempre, pero queda para compatibilidad.

public class JoinRoomRequest {
    private String roomId;
    private String jugadorNombre;

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getJugadorNombre() { return jugadorNombre; }
    public void setJugadorNombre(String jugadorNombre) { this.jugadorNombre = jugadorNombre; }
}
