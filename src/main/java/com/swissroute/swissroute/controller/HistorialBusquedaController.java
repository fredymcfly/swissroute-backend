package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.HistorialBusquedaDTO;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.service.HistorialBusquedaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historial")
public class HistorialBusquedaController {

    private final HistorialBusquedaService historialBusquedaService;

    public HistorialBusquedaController(HistorialBusquedaService historialBusquedaService) {
        this.historialBusquedaService = historialBusquedaService;
    }

    @GetMapping
    public Page<HistorialBusquedaDTO> obtenerHistorial(Pageable pageable) {
        Usuario usuario = obtenerUsuarioTemporal();

        return historialBusquedaService.obtenerHistorial(usuario, pageable);
    }

    @DeleteMapping("/{id}")
    public void eliminarEntrada(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioTemporal();

        historialBusquedaService.eliminarEntrada(id, usuario);
    }

    @DeleteMapping
    public void eliminarTodo() {
        Usuario usuario = obtenerUsuarioTemporal();

        historialBusquedaService.eliminarTodo(usuario);
    }

    /* De momento usamos  esto 
     * Porque todavía estamos simulando el usuario autenticado.
     * Más adelante, cuando tengamos  JWT/Spring Security bien montado, esto se reemplaza por el usuario real del token.
     */
    private Usuario obtenerUsuarioTemporal() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        return usuario;
    }
}