package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.FavStationDto;
import com.swissroute.swissroute.dto.StationRequest;
import com.swissroute.swissroute.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estaciones-favoritas")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/")
    @Operation(
            summary = "Registra un nuevo usuario",
            description = "Permite registrar un nuevo usuario con sus datos básicos"
    )
    public ResponseEntity<Void> addFavStation(
            @Valid @RequestBody StationRequest request, Long userId) {
        stationService.saveFavStation(request, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/")
    public ResponseEntity<List<FavStationDto>> getFavStations(Long userId) {
        List<FavStationDto> stationList = stationService.getFavStations(userId);
        return ResponseEntity.ok(stationList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id){
        stationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
