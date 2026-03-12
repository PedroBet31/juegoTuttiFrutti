/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.model;

import java.util.Objects;

// Representa a un jugador con nombre y puntaje; identidad basada solo en el nombre.
public class Jugador {

    private String nombre;
    private int puntaje;

    public Jugador() {}

    public Jugador(String nombre) {
        setNombre(nombre);
        this.puntaje = 0;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Nombre inválido");
        String n = nombre.trim();
        if (n.isEmpty()) throw new IllegalArgumentException("Nombre inválido");
        this.nombre = n;
    }

    public int getPuntaje() { return puntaje; }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void sumarPuntos(int pts) { this.puntaje += pts; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador)) return false;
        Jugador that = (Jugador) o;
        return this.nombre == null ? that.nombre == null
                : this.nombre.equalsIgnoreCase(that.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "Jugador{" + nombre + ", pts=" + puntaje + '}';
    }
}
