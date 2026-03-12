/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.model;

import java.util.ArrayList;
import java.util.List;

// Contenedor general de la partida: jugadores, rondas y configuración de puntajes/tiempos.
public class Partida {

    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<Ronda> rondas = new ArrayList<>();
    private final int tiempoPorRondaSeg;
    private final int ptsValidaUnica;
    private final int ptsValidaDuplicada;
    private int totalRondas = 1;

    public Partida() {
        this.tiempoPorRondaSeg = 30;
        this.ptsValidaUnica = 10;
        this.ptsValidaDuplicada = 5;
    }

    public Partida(int tiempoPorRondaSeg, int ptsValidaUnica, int ptsValidaDuplicada) {
        this.tiempoPorRondaSeg = tiempoPorRondaSeg;
        this.ptsValidaUnica = ptsValidaUnica;
        this.ptsValidaDuplicada = ptsValidaDuplicada;
    }

    public void agregarJugador(Jugador j) {
        if (j == null) throw new IllegalArgumentException("Jugador nulo");
        jugadores.add(j);
    }

    public List<Jugador> getJugadores() { return jugadores; }

    public void agregarRonda(Ronda r) {
        if (r == null) throw new IllegalArgumentException("Ronda nula");
        rondas.add(r);
    }

    public List<Ronda> getRondas() { return rondas; }

    public int getTiempoPorRondaSeg() { return tiempoPorRondaSeg; }
    public int getPtsValidaUnica() { return ptsValidaUnica; }
    public int getPtsValidaDuplicada() { return ptsValidaDuplicada; }

    public int getTotalRondas() { return totalRondas; }
    public void setTotalRondas(int totalRondas) { this.totalRondas = Math.max(1, totalRondas); }

    public int getRondasJugadas() { return this.rondas.size(); }

    public boolean quedanRondas() {
        return getRondasJugadas() < totalRondas;
    }
}
