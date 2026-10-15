package com.proyecto.service;

import com.proyecto.dto.ProductoDTO;
import com.proyecto.entity.Producto;
import com.proyecto.exception.ApiException;
import com.proyecto.repository.ItemCarritoRepository;
import com.proyecto.repository.ProductoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de ProductoService.
 * simuladas con Mockito: no se usa la base de datos real.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductoServiceTest {

    // Repositories falsos que Mockito crea e inyecta en el service
    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ItemCarritoRepository itemCarritoRepository;

    // Servicio real que se está probando
    @InjectMocks
    private ProductoService productoService;

    /** Prueba 1: crear un producto con un código que ya existe */
    @Test
    public void crearConCodigoRepetidoLanzaConflicto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Mouse");
        dto.setCodigo("MOU01");
        dto.setPrecio(new BigDecimal("25.50"));

        // Simulamos que el código ya existe en la base de datos
        when(productoRepository.existsByCodigo("MOU01")).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> productoService.crear(dto));

        assertEquals(HttpStatus.CONFLICT, ex.getEstado());
        // Comprobamos que nunca se llegó a guardar
        verify(productoRepository, never()).save(any());
    }

    /** Prueba 2: eliminar un producto que está en algún carrito debe dar 409 y no borrarlo. */
    @Test
    public void eliminarProductoEnUnCarritoLanzaConflicto() {
        Producto producto = new Producto();
        producto.setIdProducto(1);

        // El producto existe, y hay un carrito que lo contiene
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(itemCarritoRepository.existsByProductoIdProducto(1)).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> productoService.eliminar(1));

        assertEquals(HttpStatus.CONFLICT, ex.getEstado());
        verify(productoRepository, never()).delete(any());
    }
}