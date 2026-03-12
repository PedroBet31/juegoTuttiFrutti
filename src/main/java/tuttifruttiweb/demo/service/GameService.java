/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package tuttifruttiweb.demo.service;

// Servicio del modo individual. Maneja la partida, rondas, tiempos y delega toda
// la validación y puntaje al RoundProcessorService para mantener lógica unificada.

import tuttifruttiweb.demo.model.*;
import org.springframework.stereotype.Service;
import tuttifruttiweb.demo.service.interfaz.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class GameService {

    private Partida partida;
    private Ronda rondaActiva;
    private ScheduledFuture<?> timeoutFuture;

    private final IJuezFactory juezFactory;
    private final IGeneradorLetras genLetras;
    private final ITimeoutManager timeoutManager;
    private final IScorer scorer;
    private final IRondaFactory rondaFactory;
    private final RoundProcessorService roundProcessor;

    private List<Categoria> categoriasPartida = new ArrayList<>();

    public GameService(IJuezFactory juezFactory,
                       IGeneradorLetras genLetras,
                       ITimeoutManager timeoutManager,
                       IScorer scorer,
                       IRondaFactory rondaFactory,
                       RoundProcessorService roundProcessor) {

        this.juezFactory = juezFactory;
        this.genLetras = genLetras;
        this.timeoutManager = timeoutManager;
        this.scorer = scorer;
        this.rondaFactory = rondaFactory;
        this.roundProcessor = roundProcessor;
    }

    public synchronized void reset() {
        if (timeoutFuture != null && !timeoutFuture.isDone()) {
            timeoutManager.cancel(timeoutFuture);
        }
        timeoutFuture = null;

        this.partida = null;
        this.rondaActiva = null;
        this.categoriasPartida = new ArrayList<>();
        genLetras.reset();
    }

    public synchronized void crearPartidaSimple(String nombreJugador,
                                                List<Categoria> categorias,
                                                int tiempoSeg,
                                                int ptsUnica,
                                                int ptsDuplicada) {

        crearPartidaSimple(nombreJugador, categorias, tiempoSeg, ptsUnica, ptsDuplicada, 3);
    }

    public synchronized void crearPartidaSimple(String nombreJugador,
                                                List<Categoria> categorias,
                                                int tiempoSeg,
                                                int ptsUnica,
                                                int ptsDuplicada,
                                                int totalRondas) {

        this.partida = new Partida(tiempoSeg, ptsUnica, ptsDuplicada);
        this.partida.setTotalRondas(Math.max(1, Math.min(20, totalRondas)));

        Jugador jugador = new Jugador(nombreJugador);
        partida.agregarJugador(jugador);

        this.categoriasPartida =
                (categorias != null && !categorias.isEmpty())
                        ? new ArrayList<>(categorias)
                        : new ArrayList<>(getDefaultCategorias());

        genLetras.reset();
        this.rondaActiva = null;
    }

    public synchronized Ronda iniciarRonda() {

        if (timeoutFuture != null && !timeoutFuture.isDone())
            timeoutManager.cancel(timeoutFuture);

        timeoutFuture = null;

        if (partida == null)
            throw new IllegalStateException("Crear partida primero");

        if (!partida.quedanRondas())
            throw new IllegalStateException("No quedan rondas disponibles");

        char letra = genLetras.siguienteLetra();

        List<Categoria> categorias = categoriasPartida.isEmpty()
                ? getDefaultCategorias()
                : categoriasPartida;

        Ronda nueva = rondaFactory.crearRonda(letra, categorias, partida.getJugadores());

        this.rondaActiva = nueva;
        partida.agregarRonda(nueva);

        timeoutFuture = timeoutManager.schedule(
                this::finalizarRondaAutomatica,
                partida.getTiempoPorRondaSeg()
        );

        return nueva;
    }

    public synchronized void submitRespuestas(List<Respuesta> respuestasIngresadas, boolean usarGPT) {
        if (rondaActiva == null)
            throw new IllegalStateException("No hay ronda activa");

        if (timeoutFuture != null && !timeoutFuture.isDone())
            timeoutManager.cancel(timeoutFuture);

        roundProcessor.processRound(
                rondaActiva,
                partida,
                respuestasIngresadas,
                usarGPT
        );
    }

    private synchronized void finalizarRondaAutomatica() {
        if (rondaActiva == null) return;

        roundProcessor.processRoundAutomatic(rondaActiva, partida);
    }

    private List<Categoria> getDefaultCategorias() {
        return List.of(
                new Categoria("Nombre"),
                new Categoria("Apellido"),
                new Categoria("Animal")
        );
    }

    public synchronized Ronda getRondaActiva() {
        return rondaActiva;
    }

    public synchronized Partida getPartida() {
        return partida;
    }

    public synchronized void clearRondaActiva() {
        if (timeoutFuture != null && !timeoutFuture.isDone())
            timeoutManager.cancel(timeoutFuture);

        timeoutFuture = null;
        this.rondaActiva = null;
    }
}
