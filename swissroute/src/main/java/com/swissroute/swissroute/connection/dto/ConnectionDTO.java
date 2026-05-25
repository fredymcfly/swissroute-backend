package com.swissroute.swissroute.connection.dto;

import java.util.List;

public record ConnectionDTO(

        String origen,
        String destino,
        String duracion,
        List<String> productos,
        List<SectionDTO> secciones

) {
}