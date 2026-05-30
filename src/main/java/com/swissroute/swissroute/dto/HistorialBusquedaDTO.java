package com.swissroute.swissroute.dto;

import java.time.LocalDateTime;

public record HistorialBusquedaDTO(
        Long id,
        String origen,
        String destino,
        LocalDateTime fechaConsulta,
        Integer numResultados
) {
}