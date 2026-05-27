package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.service.TransportService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<String> getLocations(@RequestParam String query) {
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
