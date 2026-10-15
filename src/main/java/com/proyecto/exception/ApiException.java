package com.proyecto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Error de negocio con el código HTTP que debe devolver la API
 * (404 no existe, 409 conflicto, 400 petición inválida...).
 */
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus estado;

    public ApiException(HttpStatus estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }
}