package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.RutaFavoritaDTO;
import com.swissroute.swissroute.dto.RutaFavoritaRequest;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.repository.UsuarioRepository;
import com.swissroute.swissroute.service.RutaFavoritaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas-favoritas")
@Tag(
    name = "Rutas Favoritas",
    description = "Endpoints para gestionar las rutas favoritas del usuario autenticado"
)
public class RutaFavoritaController {

    private final RutaFavoritaService rutaFavoritaService;
    private final UsuarioRepository usuarioRepository;

    public RutaFavoritaController(RutaFavoritaService rutaFavoritaService, UsuarioRepository usuarioRepository) {
        this.rutaFavoritaService = rutaFavoritaService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * POST /api/rutas-favoritas
     * Crea una nueva ruta favorita
     */
    @PostMapping
    @Operation(
        summary = "Crear ruta favorita",
        description = "Guarda una ruta con nombre personalizado para acceso rápido. El nombre debe ser único para el usuario."
    )
    public ResponseEntity<RutaFavoritaDTO> crearRuta(
        @Valid @RequestBody RutaFavoritaRequest request
    ) {
        Usuario usuario = obtenerUsuarioAutenticado();
        RutaFavoritaDTO nuevaRuta = rutaFavoritaService.crearRuta(request, usuario);
        return new ResponseEntity<>(nuevaRuta, HttpStatus.CREATED);
    }

    /**
     * GET /api/rutas-favoritas
     * Obtiene todas las rutas favoritas paginadas
     */
    @GetMapping
    @Operation(
        summary = "Listar rutas favoritas",
        description = "Devuelve todas las rutas favoritas del usuario autenticado, paginadas y ordenadas por fecha de creación."
    )
    public Page<RutaFavoritaDTO> listarRutas(Pageable pageable) {
        Usuario usuario = obtenerUsuarioAutenticado();
        return rutaFavoritaService.obtenerRutas(usuario, pageable);
    }

    /**
     * GET /api/rutas-favoritas/{id}
     * Obtiene una ruta por ID
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener ruta por ID",
        description = "Devuelve los detalles de una ruta específica por su ID."
    )
    public ResponseEntity<RutaFavoritaDTO> obtenerRuta(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioAutenticado();
        RutaFavoritaDTO ruta = rutaFavoritaService.obtenerRutaPorId(id, usuario);
        return ResponseEntity.ok(ruta);
    }

    /**
     * PUT /api/rutas-favoritas/{id}
     * Actualiza una ruta existente
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar ruta favorita",
        description = "Modifica los datos de una ruta favorita existente. El nombre debe seguir siendo único para el usuario."
    )
    public ResponseEntity<RutaFavoritaDTO> actualizarRuta(
        @PathVariable Long id,
        @Valid @RequestBody RutaFavoritaRequest request
    ) {
        Usuario usuario = obtenerUsuarioAutenticado();
        RutaFavoritaDTO rutaActualizada = rutaFavoritaService.actualizarRuta(id, request, usuario);
        return ResponseEntity.ok(rutaActualizada);
    }

    /**
     * DELETE /api/rutas-favoritas/{id}
     * Elimina una ruta específica
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar ruta favorita",
        description = "Elimina una ruta específica del usuario autenticado."
    )
    public ResponseEntity<Void> eliminarRuta(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioAutenticado();
        rutaFavoritaService.eliminarRuta(id, usuario);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/rutas-favoritas
     * Elimina todas las rutas del usuario
     */
    @DeleteMapping
    @Operation(
        summary = "Eliminar todas las rutas",
        description = "Elimina todas las rutas favoritas del usuario autenticado."
    )
    public ResponseEntity<Void> eliminarTodas() {
        Usuario usuario = obtenerUsuarioAutenticado();
        rutaFavoritaService.eliminarTodasRutas(usuario);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método auxiliar para obtener el usuario autenticado
     */
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
