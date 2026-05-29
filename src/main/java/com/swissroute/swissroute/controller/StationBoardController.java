package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.StationBoard;
import com.swissroute.swissroute.service.StationboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@Tag(
        name = "Horarios",
        description = "Tablon de horarios de una estacion"
)
public class StationBoardController {

    private final StationboardService service;

    public StationBoardController(StationboardService service) {
        this.service = service;
    }

    @GetMapping("/tablon")
    public Mono<ResponseEntity<List<StationBoard>>> getTablon(
            @RequestParam String station,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String transportType) {
        return service.getStationboard(station, limit, transportType)
                .map(list -> ResponseEntity.ok().body(list));
    }

    @GetMapping("/estaciones-favoritas/{id}/tablon")
    public Mono<ResponseEntity<List<StationBoard>>> getFavoriteTablon(
            @PathVariable String id,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String transportType) {
        String stationName = resolveFavoriteStationName(id); // implementar resolución de favoritas
        return service.getStationboard(stationName, limit, transportType)
                .map(ResponseEntity::ok);
    }

    /*
    private String resolveFavoriteStationName(String id) { return "Basel SBB"; }

     */



}
