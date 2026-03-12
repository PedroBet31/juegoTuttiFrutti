/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.model.Categoria;
import tuttifruttiweb.demo.model.Jugador;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @Test
   void crearSesion_conCategoriasYLimites() {
       GameSession session = new GameSession(
               "ABC123",
               "Pedro",
               List.of(new Categoria("Animal")),
               60,
               10,
               5,
               50
       );

       assertEquals("ABC123", session.getId());
       assertEquals("Pedro", session.getOwnerName());
       assertEquals(1, session.getCategorias().size());

       assertEquals(20, session.getPartida().getTotalRondas());
   }


    @Test
    void addJugador_noDuplicaJugador() {
        GameSession session = new GameSession(
                "1", "Host", null, 60, 10, 5, 3
        );

        Jugador j1 = new Jugador("Pedro");
        Jugador j2 = new Jugador("pedro");

        session.addJugador(j1);
        session.addJugador(j2);

        assertEquals(1, session.getJugadores().size());
    }

    @Test
    void removeJugadorByName_funciona() {
        GameSession session = new GameSession(
                "1", "Host", null, 60, 10, 5, 3
        );

        session.addJugador(new Jugador("Ana"));
        session.addJugador(new Jugador("Luis"));

        session.removeJugadorByName("ana");

        assertEquals(1, session.getJugadores().size());
        assertEquals("Luis", session.getJugadores().get(0).getNombre());
    }

    @Test
    void toPlayersDTO_devuelveJugadoresYHost() {
        GameSession session = new GameSession(
                "1", "Host", null, 60, 10, 5, 3
        );

        session.addJugador(new Jugador("Ana"));
        session.addJugador(new Jugador("Luis"));

        var dto = session.toPlayersDTO();

        assertEquals(2, dto.getJugadores().size());
        assertEquals("Host", dto.getHostName());
    }
}
