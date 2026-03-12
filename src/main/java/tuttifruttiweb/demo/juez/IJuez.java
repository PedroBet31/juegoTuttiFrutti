/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tuttifruttiweb.demo.juez;

import tuttifruttiweb.demo.model.Respuesta;
import java.util.List;

// Interfaz que define el contrato básico para cualquier juez del juego (local o basado en IA).
public interface IJuez {
    void evaluarRespuestas(List<Respuesta> respuestas, char letra);
}
