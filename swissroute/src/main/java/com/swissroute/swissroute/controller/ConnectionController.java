package com.swissroute.swissroute.controller;

import com.swissroute.swissroute.dto.ConnectionDTO;
import com.swissroute.swissroute.service.ConnectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/api/conexiones")
    public List<ConnectionDTO> getConnections(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String transportations
    ) {
        return connectionService.getConnections(from, to, date, time, transportations);
    }
}
