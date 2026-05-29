package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.external.StationApiRequest;
import com.swissroute.swissroute.dto.external.StationApiResponse;
import com.swissroute.swissroute.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones-favoritas")
//@CrossOrigin(origins = "*")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/")
    @Operation(
            summary = "Registra un estacion favorita de un usuario",
            description = "Permite registrar una estacion favorita"
    )
    public ResponseEntity<Void> addFavStation(
            @Valid @RequestBody StationApiRequest request, Long userId) {
        stationService.saveFavStation(request, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Recupera estaciones favoritas de un usuario",
            description = "Permite recuperar todas las estaciones favoritas que ha añadido un usuario"
    )
    @GetMapping("/")
    public ResponseEntity<StationApiResponse>getFavStations(Long userId) {
        StationApiResponse stationList = stationService.getFavStations(userId);
        return ResponseEntity.ok(stationList);
    }

    @Operation(
            summary = "Borra una estacion favorita de un usuario",
            description = "Permite borrar una estacion favorita de un usuario"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id){
        stationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
