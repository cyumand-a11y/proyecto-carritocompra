package com.proyecto.controller;

import com.proyecto.dto.AgregarItemDTO;
import com.proyecto.dto.CarritoDTO;
import com.proyecto.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Capa de presentación: API REST del carrito anónimo.
 * El UUID del carrito viaja en la URL.
 */
@RestController
@RequestMapping("/api/v1/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Devuelve el UUID que el cliente usará en las demás peticiones
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> crear() {
        return Map.of("idCarrito", carritoService.crear());
    }

    @GetMapping("/{idCarrito}")
    public CarritoDTO obtener(@PathVariable String idCarrito) {
        return carritoService.obtener(idCarrito);
    }

    @PostMapping("/{idCarrito}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoDTO agregar(@PathVariable String idCarrito, @Valid @RequestBody AgregarItemDTO dto) {
        return carritoService.agregar(idCarrito, dto);
    }

    @DeleteMapping("/{idCarrito}/items/{idItem}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarItem(@PathVariable String idCarrito, @PathVariable Integer idItem) {
        carritoService.eliminarItem(idCarrito, idItem);
    }

    @DeleteMapping("/{idCarrito}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciar(@PathVariable String idCarrito) {
        carritoService.vaciar(idCarrito);
    }
}