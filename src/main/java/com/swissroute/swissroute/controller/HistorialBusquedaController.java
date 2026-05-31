package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.HistorialBusquedaDTO;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.repository.UsuarioRepository;
import com.swissroute.swissroute.service.HistorialBusquedaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/historial")
@Tag(
    name = "Historial de Búsquedas",
    description = "Endpoints para gestionar el historial de búsquedas de conexiones del usuario autenticado"
)
public class HistorialBusquedaController {

    private final HistorialBusquedaService historialBusquedaService;
    
    private final UsuarioRepository usuarioRepository;

    public HistorialBusquedaController(HistorialBusquedaService historialBusquedaService, UsuarioRepository usuarioRepository) {
        this.historialBusquedaService = historialBusquedaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    @Operation(
        summary = "Obtener historial de búsquedas",
        description = "Devuelve el historial paginado de búsquedas de conexiones del usuario autenticado. Cada búsqueda se registra automáticamente al consultar conexiones."
    )
    public Page<HistorialBusquedaDTO> obtenerHistorial(Pageable pageable) {
        Usuario usuario = obtenerUsuarioAutenticado();

        return historialBusquedaService.obtenerHistorial(usuario, pageable);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar una entrada del historial",
        description = "Elimina una entrada específica del historial de búsquedas por su ID. Solo el usuario propietario puede eliminar su entrada."
    )
    public void eliminarEntrada(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioAutenticado();

        historialBusquedaService.eliminarEntrada(id, usuario);
    }

    @DeleteMapping
    @Operation(
        summary = "Eliminar todo el historial",
        description = "Elimina todas las entradas del historial de búsquedas del usuario autenticado."
    )
    public void eliminarTodo() {
        Usuario usuario = obtenerUsuarioAutenticado();

        historialBusquedaService.eliminarTodo(usuario);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}