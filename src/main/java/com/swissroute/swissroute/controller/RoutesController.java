package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.FavoriteRouteDTO;
import com.swissroute.swissroute.dto.UsuarioResponse;
import com.swissroute.swissroute.service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> crearRutaFavorita(
            @Valid @RequestBody FavoriteRouteDTO request, Long userId) {


        FavoriteRouteDTO route = service.saveFavoriteRoute(request, userId);
        return new ResponseEntity<>(route, HttpStatus.CREATED);
    }
}
