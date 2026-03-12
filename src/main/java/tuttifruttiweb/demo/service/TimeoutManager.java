/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.service;

// Administrador del tiempo límite de cada ronda usando el Temporizador.

import tuttifruttiweb.demo.service.interfaz.ITimeoutManager;
import org.springframework.stereotype.Component;
import tuttifruttiweb.demo.util.Temporizador;

import java.util.concurrent.ScheduledFuture;

@Component
public class TimeoutManager implements ITimeoutManager {

    private final Temporizador temporizador;

    public TimeoutManager(Temporizador temporizador) {
        this.temporizador = temporizador;
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable tarea, int segundos) {
        return temporizador.scheduleTimeout(tarea, segundos);
    }

    @Override
    public void cancel(ScheduledFuture<?> future) {
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }
    }
}
