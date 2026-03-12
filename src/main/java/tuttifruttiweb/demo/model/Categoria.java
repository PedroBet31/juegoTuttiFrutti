/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.model;

import java.util.Objects;

// Modelo básico de categoría; su identidad es solo el nombre ignorando mayúsculas.
public class Categoria {

    private String nombre;

    public Categoria() {}

    public Categoria(String nombre) {
        setNombre(nombre);
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Nombre de categoría no puede ser nulo");
        String n = nombre.trim();
        if (n.isEmpty()) throw new IllegalArgumentException("Nombre de categoría inválido");
        this.nombre = n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return this.nombre == null ? that.nombre == null
                : this.nombre.equalsIgnoreCase(that.nombre);
    }

    @Override
    public int hashCode() {
        return nombre == null ? 0 : nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "Categoria{" + nombre + '}';
    }
}
