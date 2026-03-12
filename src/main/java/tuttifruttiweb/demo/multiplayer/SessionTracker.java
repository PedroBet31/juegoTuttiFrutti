/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

// Lleva un registro de qué sesión WebSocket pertenece a qué usuario y a qué sala.
// Permite manejar desconexiones, reconexiones y saber quién está en dónde.

@Component
public class SessionTracker {

    public record Info(String username, String roomId) {}

    private final ConcurrentHashMap<String, Info> map = new ConcurrentHashMap<>();

    public void register(String sessionId, String username, String roomId) {
        map.put(sessionId, new Info(username, roomId));
    }

    public Info getBySession(String sessionId) {
        return map.get(sessionId);
    }

    public boolean exists(String sessionId) {
        return map.containsKey(sessionId);
    }

    public void updateSession(String oldSessionId, String newSessionId) {
        Info info = map.remove(oldSessionId);
        if (info != null) {
            map.put(newSessionId, info);
        }
    }

    public Info removeBySession(String sessionId) {
        return map.remove(sessionId);
    }
}
