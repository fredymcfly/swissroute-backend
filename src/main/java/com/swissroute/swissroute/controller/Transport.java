package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.service.WebClientService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transport")
public class WebClientController {

    private final WebClientService webClientService;

    public WebClientController(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @GetMapping("/connections")
    public Mono<String> getConnections(
        @RequestParam String from,
        @RequestParam String to
    ) {
        return webClientService.getConnections(from, to);
    }

    @GetMapping("/locations")
    public Mono<String> getLocations(@RequestParam String query) {
        return webClientService.getLocations(query);
    }

    @GetMapping("/locations/coordinates")
    public Mono<String> getLocationsByCoordinates(
        @RequestParam double x,
        @RequestParam double y
    ) {
        return webClientService.getLocationsByCoordinates(x, y);
    }
}
