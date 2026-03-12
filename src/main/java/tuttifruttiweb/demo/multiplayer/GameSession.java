/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import tuttifruttiweb.demo.model.*;
import tuttifruttiweb.demo.multiplayer.dto.PlayersDTO;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

// Esta clase representa el “estado vivo” de una sala multijugador: jugadores,
// categorías, ronda activa, configuración y locks. Es básicamente el contenedor
// que mantiene todo lo que pasa dentro de una partida online.

public class GameSession {

    private final String id;
    private String ownerName;
    private final Partida partida;

    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<Categoria> categorias;

    private Ronda rondaActiva;
    private ScheduledFuture<?> timeoutFuture;

    private final ReentrantLock lock = new ReentrantLock();

    public GameSession(String id, String ownerName, List<Categoria> categorias,
                       int tiempoPorRonda, int ptsValidaUnica, int ptsValidaDuplicada, int totalRondas) {

        this.id = id;
        this.ownerName = ownerName != null ? ownerName.trim() : "Host";
        this.categorias =
                (categorias == null || categorias.isEmpty()) ?
                        List.of(new Categoria("Nombre"), new Categoria("Apellido"), new Categoria("Animal"))
                        : new ArrayList<>(categorias);

        this.partida = new Partida(tiempoPorRonda, ptsValidaUnica, ptsValidaDuplicada);
        this.partida.setTotalRondas(Math.max(1, Math.min(20, totalRondas)));
    }

    public String getId() { return id; }

    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName != null ? ownerName.trim() : this.ownerName;
    }

    public Partida getPartida() { return partida; }

    public List<Categoria> getCategorias() {
        return Collections.unmodifiableList(categorias);
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public void addJugador(Jugador j) {
        if (j != null && j.getNombre() != null) {
            String name = j.getNombre().trim();
            boolean exists = jugadores.stream().anyMatch(x -> x.getNombre().equalsIgnoreCase(name));
            if (!exists) jugadores.add(j);
        }
    }

    public void removeJugadorByName(String nombre) {
        jugadores.removeIf(j -> j.getNombre().equalsIgnoreCase(nombre));
    }

    public Ronda getRondaActiva() { return rondaActiva; }
    public void setRondaActiva(Ronda r) { this.rondaActiva = r; }

    public ScheduledFuture<?> getTimeoutFuture() { return timeoutFuture; }
    public void setTimeoutFuture(ScheduledFuture<?> future) { this.timeoutFuture = future; }

    public void lock() { lock.lock(); }
    public void unlock() { lock.unlock(); }

    public PlayersDTO toPlayersDTO() {
        List<String> names = jugadores.stream().map(Jugador::getNombre).toList();
        return new PlayersDTO(names, ownerName);
    }
}
