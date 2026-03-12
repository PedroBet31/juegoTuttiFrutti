/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void constructorAndGetSet_trimsAndReturns() {
        Categoria c = new Categoria("  Nombre  ");
        assertEquals("Nombre", c.getNombre());
    }

    @Test
    void setNombre_null_throws() {
        Categoria c = new Categoria();
        assertThrows(IllegalArgumentException.class, () -> c.setNombre(null));
    }

    @Test
    void setNombre_empty_throws() {
        Categoria c = new Categoria();
        assertThrows(IllegalArgumentException.class, () -> c.setNombre("   "));
    }

    @Test
    void equalsAndHashCode_caseInsensitive() {
        Categoria a = new Categoria("animal");
        Categoria b = new Categoria("ANIMAL");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_containsName() {
        Categoria c = new Categoria("Ciudad");
        assertTrue(c.toString().contains("Ciudad"));
    }
}
