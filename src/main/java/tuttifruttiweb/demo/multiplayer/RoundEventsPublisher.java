/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.model.Partida;
import tuttifruttiweb.demo.model.Ronda;
import tuttifruttiweb.demo.multiplayer.dto.RoundResultDTO;

// Encapsula el envío del evento round-result. Sirve para mantener ordenado el código
// del multiplayer service y separar responsabilidades.

@Component
public class RoundEventsPublisher {

    private final SimpMessagingTemplate messaging;

    public RoundEventsPublisher(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    public void publishRoundResult(String roomId, Ronda ronda, Partida partida) {

        boolean quedan = partida.quedanRondas();
        int rondaActual = partida.getRondasJugadas();
        int total = partida.getTotalRondas();

        messaging.convertAndSend(
                "/topic/room/" + roomId + "/round-result",
                new RoundResultDTO(ronda, quedan, rondaActual, total)
        );
    }
}

