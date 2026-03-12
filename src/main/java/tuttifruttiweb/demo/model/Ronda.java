/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.model;

import java.util.ArrayList;
import java.util.List;

// Representa una ronda del juego: letra asignada, categorías y todas las respuestas dadas.
public class Ronda {

    private char letra;
    private List<Categoria> categorias = new ArrayList<>();
    private List<Respuesta> respuestas = new ArrayList<>();

    public Ronda() {}

    public Ronda(char letra, List<Categoria> categorias) {
        setLetra(letra);
        if (categorias != null) this.categorias = new ArrayList<>(categorias);
        else this.categorias = new ArrayList<>();
    }

    public char getLetra() { return letra; }

    public void setLetra(char letra) {
        if (!Character.isLetter(letra))
            throw new IllegalArgumentException("Letra inválida");
        this.letra = Character.toUpperCase(letra);
    }

    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias == null ? new ArrayList<>() : categorias; }

    public List<Respuesta> getRespuestas() { return respuestas; }
    public void setRespuestas(List<Respuesta> respuestas) { this.respuestas = respuestas == null ? new ArrayList<>() : respuestas; }

    public void agregarRespuesta(Respuesta r) {
        if (r == null) return;
        respuestas.add(r);
    }

    public Respuesta getRespuesta(Jugador j, Categoria c) {
        if (j == null || c == null || respuestas == null) return null;
        return respuestas.stream()
                .filter(r -> r.getJugador() != null && r.getCategoria() != null
                        && r.getJugador().equals(j) && r.getCategoria().equals(c))
                .findFirst().orElse(null);
    }
}
