/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

import tuttifruttiweb.demo.model.Categoria;
import java.util.List;

// DTO para recibir los parámetros de creación de una sala: categorías, tiempos
// y cantidad de rondas. Representa lo que el cliente manda al crear la partida.

public class CreateRoomRequest {
    private List<Categoria> categorias;
    private int tiempoPorRonda = 60;
    private int ptsValidaUnica = 10;
    private int ptsValidaDuplicada = 5;
    private int totalRondas = 3;

    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }
    public int getTiempoPorRonda() { return tiempoPorRonda; }
    public void setTiempoPorRonda(int tiempoPorRonda) { this.tiempoPorRonda = tiempoPorRonda; }
    public int getPtsValidaUnica() { return ptsValidaUnica; }
    public void setPtsValidaUnica(int ptsValidaUnica) { this.ptsValidaUnica = ptsValidaUnica; }
    public int getPtsValidaDuplicada() { return ptsValidaDuplicada; }
    public void setPtsValidaDuplicada(int ptsValidaDuplicada) { this.ptsValidaDuplicada = ptsValidaDuplicada; }
    public int getTotalRondas() { return totalRondas; }
    public void setTotalRondas(int totalRondas) { this.totalRondas = totalRondas; }
}
