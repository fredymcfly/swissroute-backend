package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.StationDTO;
import com.swissroute.swissroute.exception.Http400Exception;
import com.swissroute.swissroute.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones")
@Tag(
        name = "Transportes",
        description = "Búsqueda de estaciones por nombre o coordenadas"
)
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping
    @Operation(
            summary = "Buscar estaciones",
            description = "Permite buscar por nombre (query) o por coordenadas (x,y)"
    )
    public Mono<List<StationDTO>> getStations(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Double x,
            @RequestParam(required = false) Double y
    ) {

        // CASO 1: búsqueda por nombre
        if (query != null && !query.isBlank()) {
            return transportService.getLocations(query);
        }

        // CASO 2: búsqueda por coordenadas
        if (x != null && y != null) {
            return transportService.getLocationsByCoordinates(x, y);
        }

        // CASO inválido
        return Mono.error(
                new Http400Exception("Debes enviar 'query' o 'x e y'")
        );
    }
}