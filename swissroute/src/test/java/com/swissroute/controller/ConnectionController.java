package com.swissroute.controller;

import com.swissroute.swissroute.connection.dto.ConnectionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conexiones")
public class ConnectionController {

    @GetMapping
    public List<ConnectionDTO> getConnections(
            @RequestParam String from,
            @RequestParam String to
    ) {
        return List.of();
    }
}