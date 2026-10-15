package com.proyecto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa la tabla "shopping_cart".
 * Cada fila es UN producto dentro del carrito de un usuario anónimo.
 */
@Entity
@Table(name = "shopping_cart")
@Getter
@Setter
@NoArgsConstructor
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idShoppingCart")
    private Integer idShoppingCart;

    // Relación con el producto (clave foránea idProducto -> producto.id_producto)
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;

    // UUID que identifica el carrito del usuario anónimo
    @Column(name = "codUsuario")
    private String codUsuario;

    private Integer cantidad;
}