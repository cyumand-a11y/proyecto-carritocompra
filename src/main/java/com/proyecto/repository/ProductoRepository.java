package com.proyecto.repository;

import com.proyecto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/** acceso a la tabla "producto".*/
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Spring genera la consulta a partir del nombre del método
    boolean existsByCodigo(String codigo);

    // Igual, pero ignora el propio producto (para validar al actualizar)
    boolean existsByCodigoAndIdProductoNot(String codigo, Integer idProducto);
}