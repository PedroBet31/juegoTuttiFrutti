/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// DTO que transporta el resultado completo de la ronda: la ronda como tal,
// si quedan rondas, índice actual y total. Es el paquete que ve el frontend
// cuando termina cada ronda en tiempo real.

import tuttifruttiweb.demo.model.Ronda;

public class RoundResultDTO {

    private Ronda ronda;
    private boolean quedanRondas;
    private int rondaActual;
    private int totalRondas;

    public RoundResultDTO(Ronda ronda, boolean quedanRondas, int rondaActual, int totalRondas) {
        this.ronda = ronda;
        this.quedanRondas = quedanRondas;
        this.rondaActual = rondaActual;
        this.totalRondas = totalRondas;
    }

    public Ronda getRonda() { return ronda; }
    public boolean isQuedanRondas() { return quedanRondas; }
    public int getRondaActual() { return rondaActual; }
    public int getTotalRondas() { return totalRondas; }
}
