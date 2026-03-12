/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service;

// Adaptador entre la fábrica original del juez (paquete juez) y la interfaz usada
// por los servicios. Solo reexpone el método getJuez.

import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.juez.JuezFactory;
import tuttifruttiweb.demo.juez.IJuez;
import tuttifruttiweb.demo.service.interfaz.IJuezFactory;

@Component
public class JuezFactoryAdapter implements IJuezFactory {

    private final JuezFactory delegate;

    public JuezFactoryAdapter(JuezFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public IJuez getJuez(boolean usarGPT) {
        return delegate.getJuez(usarGPT);
    }
}
