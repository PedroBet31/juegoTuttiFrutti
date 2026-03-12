/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

import tuttifruttiweb.demo.model.Categoria;
import java.util.List;

// DTO que notifica al cliente que una ronda comenzó. Contiene la letra,
// categorías, tiempo, número de ronda y total.

public class RoundStartedDTO {
    private char letra;
    private List<Categoria> categorias;
    private int tiempoSegundos;
    private int rondaIndex;
    private int totalRondas;

    public RoundStartedDTO() {}

    public RoundStartedDTO(char letra, List<Categoria> categorias, int tiempoSegundos, int rondaIndex, int totalRondas) {
        this.letra = letra;
        this.categorias = categorias;
        this.tiempoSegundos = tiempoSegundos;
        this.rondaIndex = rondaIndex;
        this.totalRondas = totalRondas;
    }

    public char getLetra() { return letra; }
    public void setLetra(char letra) { this.letra = letra; }

    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }

    public int getTiempoSegundos() { return tiempoSegundos; }
    public void setTiempoSegundos(int tiempoSegundos) { this.tiempoSegundos = tiempoSegundos; }

    public int getRondaIndex() { return rondaIndex; }
    public void setRondaIndex(int rondaIndex) { this.rondaIndex = rondaIndex; }

    public int getTotalRondas() { return totalRondas; }
    public void setTotalRondas(int totalRondas) { this.totalRondas = totalRondas; }
}

