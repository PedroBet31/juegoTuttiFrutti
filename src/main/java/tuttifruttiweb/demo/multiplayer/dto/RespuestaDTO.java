/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// DTO que representa una respuesta enviada por el jugador. Solo contiene texto
// y categoría, antes de transformarse en un objeto Respuesta real.

public class RespuestaDTO {
    private String categoria;
    private String texto;

    public RespuestaDTO() {}

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}

