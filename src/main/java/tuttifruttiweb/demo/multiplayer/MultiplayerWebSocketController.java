/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import tuttifruttiweb.demo.model.*;
import tuttifruttiweb.demo.multiplayer.dto.*;

// Controlador WebSocket del multijugador. Recibe mensajes del cliente (join, start,
// submit, pedir ranking) y los conecta con el servicio. También maneja la comunicación
// real-time con los tópicos STOMP.

@Controller
public class MultiplayerWebSocketController {

    private static final Logger log = LoggerFactory.getLogger(MultiplayerWebSocketController.class);

    private final MultiplayerService multiplayerService;
    private final GameSessionManager sessionManager;
    private final SimpMessageSendingOperations messaging;
    private final SessionTracker sessionTracker;

    public MultiplayerWebSocketController(
            MultiplayerService multiplayerService,
            GameSessionManager sessionManager,
            SimpMessageSendingOperations messaging,
            SessionTracker sessionTracker
    ) {
        this.multiplayerService = multiplayerService;
        this.sessionManager = sessionManager;
        this.messaging = messaging;
        this.sessionTracker = sessionTracker;
    }

    private org.springframework.messaging.MessageHeaders headersForSession(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return accessor.getMessageHeaders();
    }

    @MessageMapping("/room/{roomId}/players-ping")
    public void resendPlayers(@DestinationVariable String roomId) {

        sessionManager.get(roomId).ifPresent(session -> {
            log.info("[players-ping] reenviando players a room {}", roomId);

            messaging.convertAndSend(
                    "/topic/room/" + roomId + "/players",
                    session.toPlayersDTO()
            );
        });
    }

    @MessageMapping("/create-room")
    public void createRoom(CreateRoomRequest req,
                           @Header("simpSessionId") String sessionId,
                           Principal principal) {

        try {
            String username = (principal != null && principal.getName() != null)
                    ? principal.getName()
                    : "Jugador-" + sessionId.substring(0, 5);

            List<Categoria> categorias = req.getCategorias().stream()
                    .map(c -> new Categoria(c.getNombre()))
                    .toList();

            if (categorias.isEmpty()) {
                sendError(principal, sessionId, "Debés seleccionar al menos 1 categoría.");
                return;
            }

            if (categorias.size() > 8) {
                categorias = categorias.subList(0, 8);
                sendError(principal, sessionId, "Máximo permitido: 8 categorías. Se usarán las primeras.");
            }

            GameSession session = multiplayerService.createSession(
                    username,
                    categorias,
                    req.getTiempoPorRonda(),
                    req.getPtsValidaUnica(),
                    req.getPtsValidaDuplicada(),
                    req.getTotalRondas()
            );

            session.addJugador(new Jugador(username));

            messaging.convertAndSendToUser(
                    principal != null ? principal.getName() : sessionId,
                    "/queue/room-created",
                    new RoomCreated(session.getId()),
                    principal != null ? null : headersForSession(sessionId)
            );

            messaging.convertAndSend(
                    "/topic/room/" + session.getId() + "/players",
                    session.toPlayersDTO()
            );

            log.info("Sala {} creada por {}", session.getId(), username);

        } catch (Exception e) {
            sendError(principal, sessionId, "No se pudo crear la sala: " + e.getMessage());
        }
    }

    private void sendError(Principal principal, String sessionId, String msg) {
        messaging.convertAndSendToUser(
                principal != null ? principal.getName() : sessionId,
                "/queue/errors",
                new ErrorMessage(msg),
                principal != null ? null : headersForSession(sessionId)
        );
    }

    @MessageMapping("/room/{roomId}/join")
    public void joinRoom(
            @DestinationVariable String roomId,
            @Header("simpSessionId") String sessionId,
            SimpMessageHeaderAccessor headerAccessor,
            Principal principal) {

        String wsUser = headerAccessor.getSessionAttributes() != null
                ? (String) headerAccessor.getSessionAttributes().get("username")
                : null;

        String secUser = (principal != null && principal.getName() != null)
                ? principal.getName()
                : null;

        String username = wsUser != null
                ? wsUser
                : secUser != null
                    ? secUser
                    : "Jugador-" + sessionId.substring(0, 5);

        sessionManager.get(roomId).ifPresentOrElse(session -> {

            session.lock();
            try {
                session.addJugador(new Jugador(username));
            } finally {
                session.unlock();
            }

            sessionTracker.register(sessionId, username, roomId);

            messaging.convertAndSend(
                    "/topic/room/" + roomId + "/players",
                    session.toPlayersDTO()
            );

            log.info("Usuario {} se unió a sala {}", username, roomId);

        }, () -> messaging.convertAndSendToUser(
                principal != null ? principal.getName() : sessionId,
                "/queue/errors",
                new ErrorMessage("Sala no encontrada"),
                principal != null ? null : headersForSession(sessionId)
        ));
    }

    @MessageMapping("/room/{roomId}/start")
    public void startRound(@DestinationVariable String roomId,
                           @Header("simpSessionId") String sessionId,
                           Principal principal) {

        sessionManager.get(roomId).ifPresentOrElse(session -> {
            try {

                if (session.getJugadores().size() < 2) {
                    messaging.convertAndSendToUser(
                            principal != null ? principal.getName() : sessionId,
                            "/queue/errors",
                            new ErrorMessage("No podés iniciar la partida si estás solo en la sala."),
                            principal != null ? null : headersForSession(sessionId)
                    );
                    return; 
                }

                Ronda ronda = multiplayerService.startRound(session);

                messaging.convertAndSend(
                        "/topic/room/" + roomId + "/round-start",
                        new RoundStartedDTO(
                                ronda.getLetra(),
                                ronda.getCategorias(),
                                session.getPartida().getTiempoPorRondaSeg(),
                                session.getPartida().getRondasJugadas(),
                                session.getPartida().getTotalRondas()
                        )
                );

                log.info("Ronda iniciada en sala {}", roomId);

            } catch (Exception e) {
                messaging.convertAndSendToUser(
                        principal != null ? principal.getName() : sessionId,
                        "/queue/errors",
                        new ErrorMessage("No se pudo iniciar ronda: " + e.getMessage()),
                        principal != null ? null : headersForSession(sessionId)
                );
            }

        }, () -> messaging.convertAndSendToUser(
                principal != null ? principal.getName() : sessionId,
                "/queue/errors",
                new ErrorMessage("Sala no encontrada"),
                principal != null ? null : headersForSession(sessionId)
        ));
    }

    @MessageMapping("/room/{roomId}/submit")
    public void submitAnswers(
            @DestinationVariable String roomId,
            @Payload SubmitRequest req,
            @Header("simpSessionId") String sessionId,
            Principal principal) {

        String jugadorNombre = (principal != null && principal.getName() != null)
                ? principal.getName()
                : ("Jugador-" + sessionId.substring(0, 5));

        log.info("submitAnswers de {} en sala {}", jugadorNombre, roomId);

        sessionManager.get(roomId).ifPresentOrElse(session -> {

            try {
                List<Respuesta> respuestas = req.getRespuestas().stream()
                        .map(dto -> {
                            Respuesta r = new Respuesta();
                            r.setCategoria(new Categoria(dto.getCategoria()));
                            r.setTexto(dto.getTexto());
                            r.setJugador(new Jugador(jugadorNombre)); // nombre temporal
                            return r;
                        }).toList();

                session.lock();
                try {
                    Ronda ronda = session.getRondaActiva();
                    if (ronda == null)
                        throw new IllegalStateException("No hay ronda activa");

                    ronda.getRespuestas().removeIf(
                            r -> r.getJugador() != null
                                    && r.getJugador().getNombre() != null
                                    && r.getJugador().getNombre().equalsIgnoreCase(jugadorNombre)
                    );

                    ronda.getRespuestas().addAll(respuestas);

                } finally {
                    session.unlock();
                }

                messaging.convertAndSend("/topic/room/" + roomId + "/force-submit", "");

                new Thread(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(700);

                        session.lock();
                        List<Respuesta> acumuladas = List.copyOf(session.getRondaActiva().getRespuestas());
                        session.unlock();

                        multiplayerService.submitAnswers(session, acumuladas, req.isUsarGPT());

                        boolean quedan = session.getPartida().quedanRondas();
                        int rondaActual = session.getPartida().getRondasJugadas();
                        int total = session.getPartida().getTotalRondas();

                        messaging.convertAndSend(
                                "/topic/room/" + roomId + "/round-result",
                                new RoundResultDTO(
                                        session.getRondaActiva(),
                                        quedan,
                                        rondaActual,
                                        total
                                )
                        );

                        log.info("round-result enviado a sala {}", roomId);

                    } catch (Exception e) {
                        log.error("Error procesando submit: {}", e.getMessage(), e);
                    }
                }, "multiplayer-submit-" + roomId).start();

            } catch (Exception e) {
                messaging.convertAndSendToUser(
                        principal != null ? principal.getName() : sessionId,
                        "/queue/errors",
                        new ErrorMessage("Error procesando respuestas: " + e.getMessage()),
                        principal != null ? null : headersForSession(sessionId)
                );
            }

        }, () -> messaging.convertAndSendToUser(
                principal != null ? principal.getName() : sessionId,
                "/queue/errors",
                new ErrorMessage("Sala no encontrada"),
                principal != null ? null : headersForSession(sessionId)
        ));
    }

    @MessageMapping("/room/{roomId}/get-final")
    public void sendFinalResult(
            @DestinationVariable String roomId,
            @Header("simpSessionId") String sessionId,
            Principal principal) {

        sessionManager.get(roomId).ifPresentOrElse(session -> {

            try {
                messaging.convertAndSend(
                        "/topic/room/" + roomId + "/players",
                        session.toPlayersDTO()
                );

                log.error("DEBUG FINAL: jugadores en session {} = {}", 
                roomId,
                session.getJugadores().stream()
                       .map(Jugador::getNombre)
                       .toList()
            );

                List<Jugador> ranking = multiplayerService.buildRanking(session);

                log.info("Enviando resultado final para sala {} (jugadores: {})", roomId, ranking.size());

                messaging.convertAndSend(
                        "/topic/room/" + roomId + "/final",
                        Map.of("ranking", ranking)
                );

            } catch (Exception e) {
                messaging.convertAndSendToUser(
                        principal != null ? principal.getName() : sessionId,
                        "/queue/errors",
                        new ErrorMessage("Error generando resultado final: " + e.getMessage()),
                        principal != null ? null : headersForSession(sessionId)
                );
            }

        }, () -> messaging.convertAndSendToUser(
                principal != null ? principal.getName() : sessionId,
                "/queue/errors",
                new ErrorMessage("Sala no encontrada"),
                principal != null ? null : headersForSession(sessionId)
        ));
    }

    @MessageMapping("/room/{roomId}/go-final")
    public void goFinal(
            @DestinationVariable String roomId,
            @Header("simpSessionId") String sessionId,
            Principal principal) {

        sessionManager.get(roomId).ifPresent(session -> {
            messaging.convertAndSend(
                    "/topic/room/" + roomId + "/go-final",
                    Map.of("go", true)
            );
        });
    }
}
