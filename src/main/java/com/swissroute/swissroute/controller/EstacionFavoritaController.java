package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.EstacionFavoritaRequest;
import com.swissroute.swissroute.dto.EstacionFavoritaResponse;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.repository.UsuarioRepository;
import com.swissroute.swissroute.service.EstacionFavoritaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones-favoritas")
@Tag(
    name = "Estaciones Favoritas",
    description = "Endpoints para gestionar las estaciones favoritas del usuario autenticado"
)
public class EstacionFavoritaController {

    private final EstacionFavoritaService estacionFavoritaService;
    private final UsuarioRepository usuarioRepository;

    public EstacionFavoritaController(EstacionFavoritaService estacionFavoritaService, UsuarioRepository usuarioRepository) {
        this.estacionFavoritaService = estacionFavoritaService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * POST /api/estaciones-favoritas
     * Crea una nueva estación favorita
     */
    @PostMapping
    @Operation(
        summary = "Crear estación favorita",
        description = "Guarda una estación con nombre personalizado para acceso rápido. El ID externo debe ser único para el usuario."
    )
    @ApiResponse(responseCode = "201", description = "Estación creada")
    @ApiResponse(responseCode = "409", description = "Estación ya existe")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<EstacionFavoritaResponse> crearEstacionFavorita(
        @Valid @RequestBody EstacionFavoritaRequest request) {
        EstacionFavoritaResponse response = estacionFavoritaService.crearEstacionFavorita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/estaciones-favoritas
     * Obtiene todas las estaciones favoritas paginadas
     */
    @GetMapping
    @Operation(
        summary = "Listar estaciones favoritas",
        description = "Devuelve todas las estaciones favoritas del usuario autenticado."
    )
    public ResponseEntity<List<EstacionFavoritaResponse>> obtenerEstacionesFavoritas() {
        List<EstacionFavoritaResponse> response = estacionFavoritaService.obtenerEstacionesFavoritas();
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/estaciones-favoritas/{id}
     * Elimina una estación favorita específica
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar estación favorita",
        description = "Elimina una estación favorita específica del usuario autenticado."
    )
    @ApiResponse(responseCode = "204", description = "Estación eliminada")
    @ApiResponse(responseCode = "404", description = "Estación no encontrada")
    @ApiResponse(responseCode = "403", description = "No autorizado")
    public ResponseEntity<Void> eliminarEstacionFavorita(@PathVariable Long id) {
        estacionFavoritaService.eliminarEstacionFavorita(id);
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