/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tuttifruttiweb.demo.service.interfaz;

// Interfaz para manejar timeout asincrónico de cada ronda.

import java.util.concurrent.ScheduledFuture;

public interface ITimeoutManager {
    ScheduledFuture<?> schedule(Runnable tarea, int segundos);
    void cancel(ScheduledFuture<?> future);
}
