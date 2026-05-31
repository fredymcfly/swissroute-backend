package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.FavoriteRouteDTO;
import com.swissroute.swissroute.dto.UsuarioResponse;
import com.swissroute.swissroute.service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas-favoritas")
@Tag(
        name = "Rutas",
        description = "Controller de las rutas favoritas de un usuario"
)
public class RoutesController {

    private final RouteService service;

    public RoutesController(RouteService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<FavoriteRouteDTO> crearRutaFavorita(
            @Valid @RequestBody FavoriteRouteDTO request, Long userId) {


        FavoriteRouteDTO route = service.saveFavoriteRoute(request, userId);
        return new ResponseEntity<>(route, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteRouteDTO> actualizarRuta(
            @PathVariable Long id,
            @RequestBody FavoriteRouteDTO request,
            long userId) {

        FavoriteRouteDTO route = service.updateRoute(id, request, userId);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteRouteDTO>> obtenerRutas(Long userId) {
        return new ResponseEntity<>(service.getAllRoutes(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRuta(
            @PathVariable Long id, Long userId) {

        service.deleteRoute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
