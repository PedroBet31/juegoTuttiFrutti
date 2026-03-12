/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.model.Categoria;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Administra todas las salas multijugador creadas. Maneja crear, obtener y borrar
// sesiones de juego. Es como un “registry” central de salas activas.

@Component
public class GameSessionManager {

    private final Map<String, GameSession> sessions = new ConcurrentHashMap<>();

    public GameSession createSession(String ownerSessionId,
                                     List<Categoria> categorias,
                                     int tiempoPorRonda,
                                     int ptsValidaUnica,
                                     int ptsValidaDuplicada,
                                     int totalRondas) {

        String id = generateId();

        GameSession session = new GameSession(
                id,
                ownerSessionId,
                categorias,
                tiempoPorRonda,
                ptsValidaUnica,
                ptsValidaDuplicada,
                Math.max(1, Math.min(20, totalRondas))
        );

        sessions.put(id, session);
        return session;
    }

    public Optional<GameSession> get(String id) {
        return Optional.ofNullable(sessions.get(id));
    }

    public void remove(String id) {
        sessions.remove(id);
    }

    public List<GameSession> listAll() {
        return new ArrayList<>(sessions.values());
    }

    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
