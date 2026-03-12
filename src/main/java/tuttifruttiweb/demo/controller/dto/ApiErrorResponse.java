/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tuttifruttiweb.demo.controller.dto;

/**
 * DTO simple para devolver errores estructurados en respuestas REST.
 * Contiene un tipo de error y un mensaje explicativo.
 */
public class ApiErrorResponse {
    private String error;
    private String message;

    public ApiErrorResponse() {}
    public ApiErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

