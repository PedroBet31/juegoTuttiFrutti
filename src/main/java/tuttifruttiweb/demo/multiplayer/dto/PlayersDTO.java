/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

import java.util.ArrayList;
import java.util.List;

// DTO que representa a los jugadores actuales de la sala y cuál de ellos es el host.
// Es lo que el frontend usa para actualizar la lista de jugadores conectados.

public class PlayersDTO {
    private List<String> jugadores = new ArrayList<>();
    private String hostName; // nuevo

    public PlayersDTO() {}

    public PlayersDTO(List<String> jugadores) {
        this.jugadores = jugadores;
    }

    public PlayersDTO(List<String> jugadores, String hostName) {
        this.jugadores = jugadores;
        this.hostName = hostName;
    }

    public List<String> getJugadores() { return jugadores; }
    public void setJugadores(List<String> jugadores) { this.jugadores = jugadores; }

    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
}
