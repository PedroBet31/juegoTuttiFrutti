/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Timer;
import java.util.TimerTask;

// Escucha eventos de desconexión WebSocket. Implementa una “gracia” de reconexión:
// si el jugador vuelve rápido, no se lo expulsa; si no vuelve, se lo remueve
// correctamente de la sala.

@Component
public class WebSocketEventsListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketEventsListener.class);

    private final SessionTracker sessionTracker;
    private final GameSessionManager sessionManager;
    private final MultiplayerService multiplayerService;
    private final SimpMessagingTemplate messaging;

    private static final long GRACE_MS = 3500;

    public WebSocketEventsListener(SessionTracker sessionTracker,
                                   GameSessionManager sessionManager,
                                   MultiplayerService multiplayerService,
                                   SimpMessagingTemplate messaging) {
        this.sessionTracker = sessionTracker;
        this.sessionManager = sessionManager;
        this.multiplayerService = multiplayerService;
        this.messaging = messaging;
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {

        String sessionId = event.getSessionId();

        SessionTracker.Info info = sessionTracker.getBySession(sessionId);
        if (info == null) return;

        log.info("Jugador {} desconectado (esperando reconexión)", info.username());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (sessionTracker.exists(sessionId)) {
                    log.info("{} reconectó a tiempo, no se elimina", info.username());
                    return;
                }

                SessionTracker.Info removed = sessionTracker.removeBySession(sessionId);
                if (removed == null) return;

                log.info("Jugador {} NO reconectó, será removido de la sala {}", removed.username(), removed.roomId());

                sessionManager.get(removed.roomId()).ifPresent(session -> {

                    multiplayerService.handlePlayerLeave(session, removed.username());

                    messaging.convertAndSend(
                            "/topic/room/" + removed.roomId() + "/players",
                            session.toPlayersDTO()
                    );
                });
            }
        }, GRACE_MS);
    }
}
