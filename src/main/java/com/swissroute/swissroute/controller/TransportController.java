package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.StationDTO;
import com.swissroute.swissroute.exception.Http400Exception;
import com.swissroute.swissroute.service.TransportService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }



    @GetMapping("/connections")
    public Mono<String> getConnections(
        @RequestParam String from,
        @RequestParam String to
    ) {
        return transportService.getConnections(from, to);
    }

    @GetMapping("/locations")
    public Mono<List<StationDTO>> getStations(@RequestParam(required = false) String query) {

        if (query == null || query.isBlank()) {
            return Mono.error(
                    new Http400Exception("El parámetro 'query' es obligatorio")
            );
        }

        return transportService.getLocations(query);
    }

    @GetMapping("/locations/coordinates")
    public Mono<String> getLocationsByCoordinates(
        @RequestParam double x,
        @RequestParam double y
    ) {
        return transportService.getLocationsByCoordinates(x, y);
    }
}
