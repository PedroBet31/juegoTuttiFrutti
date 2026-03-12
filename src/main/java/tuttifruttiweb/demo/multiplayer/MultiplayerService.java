/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tuttifruttiweb.demo.model.*;
import tuttifruttiweb.demo.service.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;
import tuttifruttiweb.demo.juez.JuezFactory;

// Servicio principal del modo multijugador. Acá vive toda la lógica pesada:
// iniciar rondas, procesar respuestas, asignar puntos, evitar duplicaciones,
// manejar timeout, publicar eventos y armar el ranking final.

@Service
public class MultiplayerService {

    private static final Logger log = LoggerFactory.getLogger(MultiplayerService.class);

    private final GameSessionManager sessionManager;
    private final RondaFactory rondaFactory;
    private final JuezFactory juezFactory;
    private final Scorer scorer;
    private final GeneradorLetrasService generadorLetrasService;
    private final TimeoutManager timeoutManager;
    private final RoundProcessorService roundProcessor;
    private final RoundEventsPublisher eventsPublisher;
    private final SimpMessagingTemplate messaging;

    private final ConcurrentHashMap<String, AtomicBoolean> processing = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Set<Ronda>> appliedRoundsBySession = new ConcurrentHashMap<>();

    public MultiplayerService(GameSessionManager sessionManager,
                              RondaFactory rondaFactory,
                              JuezFactory juezFactory,
                              Scorer scorer,
                              GeneradorLetrasService generadorLetrasService,
                              TimeoutManager timeoutManager,
                              RoundProcessorService roundProcessor,
                              RoundEventsPublisher eventsPublisher,
                              SimpMessagingTemplate messaging) {
        this.sessionManager = sessionManager;
        this.rondaFactory = rondaFactory;
        this.juezFactory = juezFactory;
        this.scorer = scorer;
        this.generadorLetrasService = generadorLetrasService;
        this.timeoutManager = timeoutManager;
        this.roundProcessor = roundProcessor;
        this.eventsPublisher = eventsPublisher;
        this.messaging = messaging;
    }

    public GameSession createSession(String ownerName,
                                     List<Categoria> categorias,
                                     int tiempoPorRonda,
                                     int ptsValidaUnica,
                                     int ptsValidaDuplicada,
                                     int totalRondas) {

        return sessionManager.createSession(
                ownerName,
                categorias,
                tiempoPorRonda,
                ptsValidaUnica,
                ptsValidaDuplicada,
                totalRondas
        );
    }

    public Ronda startRound(GameSession session) {

        session.lock();
        try {
            Partida partida = session.getPartida();
            if (!partida.quedanRondas())
                throw new IllegalStateException("No quedan rondas");

            char letra = generadorLetrasService.siguienteLetra();

            List<Categoria> cats = session.getCategorias().isEmpty()
                    ? List.of(new Categoria("Nombre"),
                              new Categoria("Apellido"),
                              new Categoria("Animal"))
                    : session.getCategorias();

            Ronda r = rondaFactory.crearRonda(letra, cats, session.getJugadores());

            session.setRondaActiva(r);
            partida.agregarRonda(r);

            appliedRoundsBySession.computeIfAbsent(session.getId(), k -> ConcurrentHashMap.newKeySet());

            ScheduledFuture<?> f = timeoutManager.schedule(
                    () -> finalizeRoundAutomatic(session.getId()),
                    partida.getTiempoPorRondaSeg()
            );

            session.setTimeoutFuture(f);

            log.info("Ronda iniciada en sala {} - letra={} tiempo={}s (ronda {}/{})",
                    session.getId(),
                    letra,
                    partida.getTiempoPorRondaSeg(),
                    partida.getRondasJugadas(),
                    partida.getTotalRondas()
            );

            return r;

        } finally {
            session.unlock();
        }
    }

    public void submitAnswers(GameSession session,
                              List<Respuesta> respuestasIngresadas,
                              boolean usarGPT) {

        AtomicBoolean flag = processing.computeIfAbsent(session.getId(), id -> new AtomicBoolean(false));

        if (!flag.compareAndSet(false, true)) {
            log.warn("submitAnswers ignorado: sesión {} ya está siendo procesada", session.getId());
            return;
        }

        session.lock();
        try {

            if (session.getRondaActiva() == null)
                throw new IllegalStateException("No hay ronda activa");

            ScheduledFuture<?> f = session.getTimeoutFuture();
            if (f != null && !f.isDone()) timeoutManager.cancel(f);
            session.setTimeoutFuture(null);

            log.info("Procesando submitAnswers para sesión {} (jugadores: {})",
                    session.getId(),
                    session.getJugadores().size());

            if (respuestasIngresadas != null) {
                respuestasIngresadas.forEach(r -> {
                    if (r.getJugador() != null && r.getJugador().getNombre() != null) {
                        String nombre = r.getJugador().getNombre().trim();
                        session.getJugadores().stream()
                                .filter(j -> j.getNombre().equalsIgnoreCase(nombre))
                                .findFirst()
                                .ifPresent(r::setJugador);
                    }
                });
            }

            roundProcessor.processRound(
                    session.getRondaActiva(),
                    session.getPartida(),
                    respuestasIngresadas,
                    usarGPT
            );

            Set<Ronda> applied = appliedRoundsBySession.computeIfAbsent(session.getId(), k -> ConcurrentHashMap.newKeySet());
            Ronda current = session.getRondaActiva();

            if (!applied.contains(current)) {
                for (Respuesta r : current.getRespuestas()) {
                    if (r.getJugador() == null || r.getJugador().getNombre() == null) continue;
                    String nombre = r.getJugador().getNombre().trim();
                    session.getJugadores().stream()
                            .filter(j -> j.getNombre().equalsIgnoreCase(nombre))
                            .findFirst()
                            .ifPresent(j -> {
                                int antes = j.getPuntaje();
                                j.setPuntaje(antes + r.getPuntos());
                            });
                }
                applied.add(current);
                log.debug("Puntos aplicados para ronda (session={}, ronda={}): ok", session.getId(), current.getLetra());
            } else {
                log.debug("Ronda ya había sido aplicada (session={}, letra={}) -> no se vuelven a sumar puntos",
                        session.getId(),
                        current.getLetra());
            }

            eventsPublisher.publishRoundResult(
                    session.getId(),
                    session.getRondaActiva(),
                    session.getPartida()
            );

            messaging.convertAndSend(
                    "/topic/room/" + session.getId() + "/players",
                    session.toPlayersDTO()
            );

        } finally {
            flag.set(false);
            session.unlock();
        }
    }

    public void finalizeRoundAutomatic(String sessionId) {

        AtomicBoolean flag = processing.computeIfAbsent(sessionId, id -> new AtomicBoolean(false));
        if (!flag.compareAndSet(false, true)) {
            log.debug("finalizeRoundAutomatic ignorado para sesión {}", sessionId);
            return;
        }

        sessionManager.get(sessionId).ifPresent(session -> {
            session.lock();
            try {

                if (session.getRondaActiva() != null) {

                    roundProcessor.processRoundAutomatic(
                            session.getRondaActiva(),
                            session.getPartida()
                    );

                    Set<Ronda> applied = appliedRoundsBySession.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet());
                    Ronda current = session.getRondaActiva();

                    if (!applied.contains(current)) {
                        for (Respuesta r : current.getRespuestas()) {
                            if (r.getJugador() == null || r.getJugador().getNombre() == null) continue;
                            String nombre = r.getJugador().getNombre().trim();
                            session.getJugadores().stream()
                                    .filter(j -> j.getNombre().equalsIgnoreCase(nombre))
                                    .findFirst()
                                    .ifPresent(j -> j.setPuntaje(j.getPuntaje() + r.getPuntos()));
                        }
                        applied.add(current);
                    } else {
                        log.debug("finalizeRoundAutomatic: puntos ya aplicados previamente para session {} letra {}",
                                sessionId, current.getLetra());
                    }

                    eventsPublisher.publishRoundResult(
                            sessionId,
                            session.getRondaActiva(),
                            session.getPartida()
                    );

                    messaging.convertAndSend(
                            "/topic/room/" + sessionId + "/players",
                            session.toPlayersDTO()
                    );
                }

            } finally {
                flag.set(false);
                session.unlock();
            }
        });
    }

    public List<Jugador> buildRanking(GameSession session) {
        List<Jugador> players = session.getJugadores();
        if (players == null || players.isEmpty()) {
            log.warn("buildRanking: sala {} sin jugadores (devuelve lista vacía)", session.getId());
            return List.of();
        }

        return players.stream()
                .sorted((a, b) -> Integer.compare(b.getPuntaje(), a.getPuntaje()))
                .toList();
    }

    public void handlePlayerLeave(GameSession session, String username) {

        session.lock();
        try {
            String currentHost = session.getOwnerName();
            boolean eraHost = currentHost != null && currentHost.equalsIgnoreCase(username);

            session.removeJugadorByName(username);

            log.info("Jugador {} salió de la sala {}", username, session.getId());

            sendSystemMessage(session.getId(), username + " abandonó la sala");

            messaging.convertAndSend(
                    "/topic/room/" + session.getId() + "/players",
                    session.toPlayersDTO()
            );

            if (eraHost) {
                List<Jugador> jugadores = session.getJugadores();
                if (!jugadores.isEmpty()) {
                    String nuevoHost = jugadores.get(0).getNombre();
                    session.setOwnerName(nuevoHost);

                    sendSystemMessage(session.getId(), "El nuevo host es ahora " + nuevoHost);

                    messaging.convertAndSend(
                            "/topic/room/" + session.getId() + "/players",
                            session.toPlayersDTO()
                    );

                    log.info("Host reasignado en sala {}: {} -> {}", session.getId(), username, nuevoHost);
                } else {
                    log.info("Sala {} quedó vacía después de que se fuera el host {}", session.getId(), username);
                }
            }

        } finally {
            session.unlock();
        }
    }

    private void sendSystemMessage(String roomId, String message) {
        Map<String, String> payload = Map.of("message", message);
        messaging.convertAndSend("/topic/room/" + roomId + "/system", payload);
    }
}
