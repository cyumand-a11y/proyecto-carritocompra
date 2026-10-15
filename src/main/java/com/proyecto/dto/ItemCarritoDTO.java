package com.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Un renglón del carrito en la respuesta: producto, cantidad y subtotal.
 * @AllArgsConstructor crea un constructor con todos los campos.
 */
@Getter
@AllArgsConstructor
public class ItemCarritoDTO {
    private Integer idItem;
    private Integer idProducto;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidad;
    private BigDecimal subtotal;   // precio x cantidad
}