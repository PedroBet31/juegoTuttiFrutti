/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller.dto;

import tuttifruttiweb.demo.model.Categoria;
import tuttifruttiweb.demo.model.Respuesta;
import tuttifruttiweb.demo.model.Ronda;

import java.util.List;

/**
 * DTO que empaqueta los datos de una ronda para enviarlos al front
 * en el modo individual: letra, categorías, respuestas y tiempo.
 */
public class RoundResponse {
    private char letra;
    private List<Categoria> categorias;
    private List<Respuesta> respuestas;
    private int tiempo;

    public static RoundResponse fromRonda(Ronda r, int tiempo) {
        RoundResponse rr = new RoundResponse();
        rr.letra = r.getLetra();
        rr.categorias = r.getCategorias();
        rr.respuestas = r.getRespuestas();
        rr.tiempo = tiempo;
        return rr;
    }

    public char getLetra() { return letra; }
    public List<Categoria> getCategorias() { return categorias; }
    public List<Respuesta> getRespuestas() { return respuestas; }
    public int getTiempo() { return tiempo; }
}
