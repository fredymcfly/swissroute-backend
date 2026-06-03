package com.swissroute.swissroute.controller;




import com.swissroute.swissroute.service.TransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import com.swissroute.swissroute.dto.ConnectionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<ConnectionDTO> getConnections(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String transportations
    ) {
        return transportService.getConnections(from, to, date, time, transportations);
    }
}
