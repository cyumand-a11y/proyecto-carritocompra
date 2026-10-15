package com.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Objeto que viaja por la API (entrada y salida) para un producto.
 * Evita exponer la entidad de la base de datos directamente.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductoDTO {

    // Solo se usa en las respuestas; al crear se ignora
    private Integer idProducto;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    private String imagen;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio;
}

