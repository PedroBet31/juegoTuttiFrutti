/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tuttifruttiweb.demo.controller.dto.ApiErrorResponse;

/**
 * Handler global de errores. Unifica las respuestas JSON para errores REST
 * y muestra una vista genérica para fallos inesperados en MVC.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseBody
    public ResponseEntity<ApiErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        ApiErrorResponse body = new ApiErrorResponse("Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({ IllegalStateException.class })
    @ResponseBody
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        ApiErrorResponse body = new ApiErrorResponse("Illegal State", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception ex) {
        log.error("Error no esperado", ex);
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", "Ocurrió un error interno.");
        return mv;
    }
}
