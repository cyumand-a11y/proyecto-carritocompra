package com.proyecto.service;

import com.proyecto.dto.ProductoDTO;
import com.proyecto.entity.Producto;
import com.proyecto.exception.ApiException;
import com.proyecto.repository.ItemCarritoRepository;
import com.proyecto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de negocio: reglas de los productos.
 * @RequiredArgsConstructor crea el constructor que recibe los repositories.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ItemCarritoRepository itemCarritoRepository;

    /** Devuelve todos los productos. */
    public List<ProductoDTO> listar() {
        return productoRepository.findAll().stream().map(this::aDto).toList();
    }

    /** Devuelve un producto o 404 si no existe. */
    public ProductoDTO obtener(Integer id) {
        return aDto(buscar(id));
    }

    /** Crea un producto; el código no puede repetirse (409). */
    public ProductoDTO crear(ProductoDTO dto) {
        if (productoRepository.existsByCodigo(dto.getCodigo())) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con el código " + dto.getCodigo());
        }
        return aDto(productoRepository.save(aEntidad(new Producto(), dto)));
    }

    /** Actualiza un producto; el código no puede coincidir con el de otro (409). */
    public ProductoDTO actualizar(Integer id, ProductoDTO dto) {
        Producto producto = buscar(id);
        if (productoRepository.existsByCodigoAndIdProductoNot(dto.getCodigo(), id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe otro producto con el código " + dto.getCodigo());
        }
        return aDto(productoRepository.save(aEntidad(producto, dto)));
    }

    /** Elimina un producto; si está en algún carrito se rechaza (409). */
    public void eliminar(Integer id) {
        Producto producto = buscar(id);
        if (itemCarritoRepository.existsByProductoIdProducto(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "No se puede eliminar: el producto está en un carrito");
        }
        productoRepository.delete(producto);
    }

    // Busca por id o lanza 404
    private Producto buscar(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + id));
    }

    // Entidad -> DTO
    private ProductoDTO aDto(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(p.getIdProducto());
        dto.setNombre(p.getNombre());
        dto.setCodigo(p.getCodigo());
        dto.setImagen(p.getImagen());
        dto.setPrecio(p.getPrecio());
        return dto;
    }

    // Copia los datos del DTO a la entidad (el id no se toca)
    private Producto aEntidad(Producto p, ProductoDTO dto) {
        p.setNombre(dto.getNombre());
        p.setCodigo(dto.getCodigo());
        p.setImagen(dto.getImagen());
        p.setPrecio(dto.getPrecio());
        return p;
    }
}