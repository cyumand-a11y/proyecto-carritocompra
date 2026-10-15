package com.proyecto.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Captura los errores de toda la API y los convierte en JSON: {"error": "mensaje"}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de negocio lanzados desde el service
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> manejarApi(ApiException ex) {
        return ResponseEntity.status(ex.getEstado()).body(Map.of("error", ex.getMessage()));
    }

    // Errores de validación (@NotBlank, @Positive...) -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}