package com.proyecto.repository;

import com.proyecto.entity.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**acceso a la tabla "shopping_cart".*/
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Integer> {

    // ¿Hay algún carrito que contenga este producto? (para no borrarlo si está en uso)
    boolean existsByProductoIdProducto(Integer idProducto);

    // Todos los ítems de un carrito
    List<ItemCarrito> findByCodUsuario(String codUsuario);

    // El ítem de un producto concreto dentro de un carrito (si ya estaba agregado)
    Optional<ItemCarrito> findByCodUsuarioAndProductoIdProducto
    (String codUsuario, Integer idProducto);
}