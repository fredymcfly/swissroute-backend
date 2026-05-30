package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.StationBoardDTO;
import com.swissroute.swissroute.service.StationBoardServiceImpl;
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

    private final StationBoardServiceImpl service;

    public StationBoardController(StationBoardServiceImpl service) {
        this.service = service;
    }


    @GetMapping("/tablon")
    public Mono<List<StationBoardDTO>> getTablon(
            @RequestParam String station,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) List<String> transport
    ) {
        return service.getStationboard(station, limit, transport);
    }

    /*
    @GetMapping("/estaciones-favoritas/{id}/tablon")
    public Mono<ResponseEntity<List<StationBoardDTO>>> getFavoriteTablon(
            @PathVariable String id,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String transportType) {
        String stationName = resolveFavoriteStationName(id); // implementar resolución de favoritas
        return service.getStationboard(stationName, limit, transportType)
                .map(ResponseEntity::ok);
    }

     */

    /*
    private String resolveFavoriteStationName(String id) { return "Basel SBB"; }

     */



}
