/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// Respuesta que recibe el cliente cuando se crea una sala indicando su ID.

public class RoomCreated {
    private String roomId;
    public RoomCreated() {}
    public RoomCreated(String roomId) { this.roomId = roomId; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}
