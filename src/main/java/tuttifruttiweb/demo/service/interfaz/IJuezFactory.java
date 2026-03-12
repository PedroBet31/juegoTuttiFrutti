/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tuttifruttiweb.demo.service.interfaz;

// Interfaz adaptadora para obtener un juez según si se usa GPT o no.

import tuttifruttiweb.demo.juez.IJuez;

public interface IJuezFactory {
    IJuez getJuez(boolean usarGPT);
}
