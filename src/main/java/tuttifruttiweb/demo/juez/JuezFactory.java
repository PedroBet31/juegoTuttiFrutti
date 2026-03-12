/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.juez;

import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.juez.local.LocalJuezService;
import tuttifruttiweb.demo.juez.gpt.GroqJuezService;

// Fábrica simple que decide qué juez usar según si se habilita o no el uso de GPT.
@Component
public class JuezFactory {

    private final LocalJuezService local;
    private final GroqJuezService gpt;

    public JuezFactory(LocalJuezService local, GroqJuezService gpt) {
        this.local = local;
        this.gpt = gpt;
    }

    public IJuez getJuez(boolean usarGPT) {
        return usarGPT ? gpt : local;
    }
}
