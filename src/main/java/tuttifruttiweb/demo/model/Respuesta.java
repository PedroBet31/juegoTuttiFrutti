/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.model;

// Respuesta de un jugador en una categoría, con texto, veredicto y puntos asignados.
public class Respuesta {

    private Jugador jugador;
    private Categoria categoria;
    private String texto = "";
    private Veredicto veredicto = Veredicto.VACIA;
    private String motivo = "";
    private int puntos = 0;

    public enum Veredicto { VACIA, INVALIDA, VALIDA, VALIDA_DUPLICADA }

    public Respuesta() {}

    public Respuesta(Jugador jugador, Categoria categoria) {
        this.jugador = jugador;
        this.categoria = categoria;
    }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto == null ? "" : texto.trim(); }

    public Veredicto getVeredicto() { return veredicto; }
    public void setVeredicto(Veredicto veredicto) { this.veredicto = veredicto == null ? Veredicto.VACIA : veredicto; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo == null ? "" : motivo; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    @Override
    public String toString() {
        return "Respuesta{" +
                "jugador=" + (jugador == null ? "null" : jugador.getNombre()) +
                ", categoria=" + (categoria == null ? "null" : categoria.getNombre()) +
                ", texto='" + texto + '\'' +
                ", veredicto=" + veredicto +
                ", puntos=" + puntos +
                '}';
    }
}
