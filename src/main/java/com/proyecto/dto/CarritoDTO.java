package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Respuesta completa del carrito: sus ítems y el total a pagar.
 */
@Getter
@AllArgsConstructor
public class CarritoDTO {
    private String idCarrito;
    private List<ItemCarritoDTO> items;
    private BigDecimal total;
}