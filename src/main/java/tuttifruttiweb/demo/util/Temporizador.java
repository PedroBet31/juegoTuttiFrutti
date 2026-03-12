/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.util;

// Temporizador general del server. Ejecuta tareas después de N segundos usando un scheduler propio.

import org.springframework.stereotype.Component;
import java.util.concurrent.*;

@Component
public class Temporizador {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ScheduledFuture<?> scheduleTimeout(Runnable tarea, int segundos) {
        return scheduler.schedule(tarea, Math.max(0, segundos), TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
