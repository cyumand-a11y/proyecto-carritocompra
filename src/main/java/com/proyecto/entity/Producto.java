package com.proyecto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidad que representa la tabla "producto".
 * Lombok genera los getters, setters y el constructor vacío.
 */
@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    // Clave primaria autoincremental (AUTO_INCREMENT en MariaDB)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    private String nombre;

    // Código del producto; su unicidad se valida en el service
    private String codigo;

    // Ruta relativa de la imagen (ej: images/product/camera.jpg)
    private String imagen;

    // BigDecimal evita errores de redondeo al trabajar con dinero
    private BigDecimal precio;
}