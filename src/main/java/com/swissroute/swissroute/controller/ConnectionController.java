package com.swissroute.swissroute.controller;


import com.swissroute.swissroute.exception.Http400Exception;
import com.swissroute.swissroute.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/conexiones")
@Tag(
        name = "Conexiones",
        description = "Conexion entre estaciones"
)
public class ConnectionController {

    private final TransportService transportService;

    public ConnectionController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping(params = {"from", "to"})
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
}
