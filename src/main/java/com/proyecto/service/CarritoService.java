package com.proyecto.service;

import com.proyecto.dto.AgregarItemDTO;
import com.proyecto.dto.CarritoDTO;
import com.proyecto.dto.ItemCarritoDTO;
import com.proyecto.entity.ItemCarrito;
import com.proyecto.entity.Producto;
import com.proyecto.exception.ApiException;
import com.proyecto.repository.ItemCarritoRepository;
import com.proyecto.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

/**
 * Capa de negocio: reglas del carrito anónimo.
 * El carrito se identifica con un UUID, que se guarda en la columna codUsuario.
 */
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final ItemCarritoRepository itemRepository;
    private final ProductoRepository productoRepository;

    /** Genera el UUID de un carrito nuevo. No se guarda nada hasta agregar el primer ítem. */
    public String crear() {
        return UUID.randomUUID().toString();
    }

    /** Devuelve el carrito con subtotales y total. Un carrito sin ítems devuelve lista vacía. */
    public CarritoDTO obtener(String idCarrito) {
        validarUuid(idCarrito);
        List<ItemCarritoDTO> items = itemRepository.findByCodUsuario(idCarrito).stream()
                .map(this::aDto)
                .toList();
        // Total = suma de los subtotales
        BigDecimal total = items.stream()
                .map(ItemCarritoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CarritoDTO(idCarrito, items, total);
    }

    /** Agrega un producto; si ya estaba en el carrito, suma la cantidad en lugar de duplicar. */
    public CarritoDTO agregar(String idCarrito, AgregarItemDTO dto) {
        validarUuid(idCarrito);
        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado: " + dto.getIdProducto()));

        // Si el producto ya está en este carrito se reutiliza el ítem; si no, se crea uno nuevo
        ItemCarrito item = itemRepository
                .findByCodUsuarioAndProductoIdProducto(idCarrito, producto.getIdProducto())
                .orElseGet(() -> {
                    ItemCarrito nuevo = new ItemCarrito();
                    nuevo.setCodUsuario(idCarrito);
                    nuevo.setProducto(producto);
                    nuevo.setCantidad(0);
                    return nuevo;
                });
        item.setCantidad(item.getCantidad() + dto.getCantidad());
        itemRepository.save(item);
        return obtener(idCarrito);
    }

    /** Elimina un ítem, solo si pertenece a este carrito (evita borrar ítems ajenos). */
    public void eliminarItem(String idCarrito, Integer idItem) {
        validarUuid(idCarrito);
        ItemCarrito item = itemRepository.findById(idItem)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Ítem no encontrado: " + idItem));
        if (!item.getCodUsuario().equals(idCarrito)) {
            // Se responde 404 para no revelar que el ítem existe en otro carrito
            throw new ApiException(HttpStatus.NOT_FOUND, "Ítem no encontrado en este carrito: " + idItem);
        }
        itemRepository.delete(item);
    }

    /** Elimina todos los ítems del carrito. */
    public void vaciar(String idCarrito) {
        validarUuid(idCarrito);
        itemRepository.deleteAll(itemRepository.findByCodUsuario(idCarrito));
    }

    // Rechaza con 400 los identificadores que no tengan formato UUID
    private void validarUuid(String idCarrito) {
        try {
            UUID.fromString(idCarrito);
        } catch (IllegalArgumentException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "El idCarrito no es un UUID válido");
        }
    }

    // Entidad -> DTO, calculando el subtotal con 2 decimales
    private ItemCarritoDTO aDto(ItemCarrito i) {
        BigDecimal subtotal = i.getProducto().getPrecio()
                .multiply(BigDecimal.valueOf(i.getCantidad()))
                .setScale(2, RoundingMode.HALF_UP);
        return new ItemCarritoDTO(i.getIdShoppingCart(), i.getProducto().getIdProducto(),
                i.getProducto().getNombre(), i.getProducto().getPrecio(), i.getCantidad(), subtotal);
    }
}