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
@RequestMapping("/api/transport")
@Tag(
        name = "Transportes",
        description = "Operaciones relacionadas con transporte, estaciones y conexiones"
)
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("/connections")
    @Operation(
            summary = "Obtener conexiones entre dos estaciones",
            description = "Devuelve las conexiones de transporte entre dos ubicaciones (from → to)"
    )
    public Mono<String> getConnections(
            @RequestParam String from,
            @RequestParam String to
    ) {
        return transportService.getConnections(from, to);
    }

    @GetMapping("/locations")
    @Operation(
            summary = "Buscar estaciones por nombre",
            description = "Devuelve una lista de estaciones filtradas por nombre (query obligatorio)"
    )
    public Mono<List<StationDTO>> getStations(
            @RequestParam(required = false) String query
    ) {

        if (query == null || query.isBlank()) {
            return Mono.error(
                    new Http400Exception("El parámetro 'query' es obligatorio")
            );
        }

        return transportService.getLocations(query);
    }

    @GetMapping("/locations/coordinates")
    @Operation(
            summary = "Buscar estaciones por coordenadas",
            description = "Devuelve estaciones cercanas usando coordenadas X (lat) e Y (lon)"
    )
    public Mono<String> getLocationsByCoordinates(
            @RequestParam double x,
            @RequestParam double y
    ) {
        return transportService.getLocationsByCoordinates(x, y);
    }
}