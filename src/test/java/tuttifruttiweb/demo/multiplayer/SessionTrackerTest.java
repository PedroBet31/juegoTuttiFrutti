/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.multiplayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTrackerTest {

    @Test
    void register_y_get_funciona() {
        SessionTracker tracker = new SessionTracker();

        tracker.register("S1", "Pedro", "ROOM1");

        SessionTracker.Info info = tracker.getBySession("S1");

        assertNotNull(info);
        assertEquals("Pedro", info.username());
        assertEquals("ROOM1", info.roomId());
    }

    @Test
    void exists_funciona() {
        SessionTracker tracker = new SessionTracker();

        tracker.register("S1", "Pedro", "ROOM1");

        assertTrue(tracker.exists("S1"));
        assertFalse(tracker.exists("S2"));
    }

    @Test
    void updateSession_mueveInfo() {
        SessionTracker tracker = new SessionTracker();

        tracker.register("OLD", "Pedro", "ROOM1");
        tracker.updateSession("OLD", "NEW");

        assertNull(tracker.getBySession("OLD"));
        assertNotNull(tracker.getBySession("NEW"));
    }

    @Test
    void removeBySession_elimina() {
        SessionTracker tracker = new SessionTracker();

        tracker.register("S1", "Pedro", "ROOM1");
        var removed = tracker.removeBySession("S1");

        assertNotNull(removed);
        assertFalse(tracker.exists("S1"));
    }
}
