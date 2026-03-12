/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service;

// Genera letras aleatorias sin repetir para cada ronda.

import tuttifruttiweb.demo.service.interfaz.IGeneradorLetras;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GeneradorLetrasService implements IGeneradorLetras {
    private final List<Character> usadas = new ArrayList<>();
    private final Random rnd = new Random();

    @Override
    public synchronized char siguienteLetra() {
        if (usadas.size() >= 26) throw new IllegalStateException("Se agotaron las letras");
        char c;
        do {
            c = (char) ('A' + rnd.nextInt(26));
        } while (usadas.contains(c));
        usadas.add(c);
        return c;
    }

    @Override
    public synchronized void reset() { usadas.clear(); }
}
