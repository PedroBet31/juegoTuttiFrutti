/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.junit.jupiter.api.Test;
import tuttifruttiweb.demo.controller.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;

class GlobalControllerAdviceTest {

    private final GlobalControllerAdvice advice = new GlobalControllerAdvice();

    @Test
    void handleBadRequest_buildsResponse() {
        ResponseEntity<ApiErrorResponse> r = advice.handleBadRequest(new IllegalArgumentException("bad"));
        assertEquals(400, r.getStatusCodeValue());
        assertNotNull(r.getBody());
        assertEquals("Bad Request", r.getBody().getError());
    }

    @Test
    void handleIllegalState_buildsResponse() {
        ResponseEntity<ApiErrorResponse> r = advice.handleIllegalState(new IllegalStateException("s"));
        assertEquals(409, r.getStatusCodeValue());
        assertNotNull(r.getBody());
        assertEquals("Illegal State", r.getBody().getError());
    }

    @Test
    void handleAnyException_returnsModelAndView() {
        ModelAndView mv = advice.handleAnyException(new RuntimeException("x"));
        assertNotNull(mv);
        assertEquals("error", mv.getViewName());
        assertEquals("Ocurrió un error interno.", mv.getModel().get("message"));
    }
}
