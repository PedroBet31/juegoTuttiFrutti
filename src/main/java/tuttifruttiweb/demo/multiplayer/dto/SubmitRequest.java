/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer.dto;

// DTO que representa el envío de respuestas de un jugador durante la ronda.
// También indica si debe usarse GPT para la validación.

import java.util.List;

public class SubmitRequest {
    private List<RespuestaDTO> respuestas;
    private boolean usarGPT = true;

    public SubmitRequest() {}

    public List<RespuestaDTO> getRespuestas() { return respuestas; }
    public void setRespuestas(List<RespuestaDTO> respuestas) { this.respuestas = respuestas; }

    public boolean isUsarGPT() { return usarGPT; }
    public void setUsarGPT(boolean usarGPT) { this.usarGPT = usarGPT; }
}
