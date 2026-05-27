package com.swissroute.swissroute.mapper;

import com.swissroute.swissroute.dto.ConnectionDTO;
import com.swissroute.swissroute.dto.SectionDTO;
import com.swissroute.swissroute.dto.external.ExternalConnection;
import com.swissroute.swissroute.dto.external.ExternalSection;

import java.util.List;

public class ConnectionMapper {

    public static ConnectionDTO toDTO(ExternalConnection connection) {
        List<SectionDTO> secciones = connection.sections() == null
                ? List.of()
                : connection.sections()
                    .stream()
                    .map(ConnectionMapper::toSectionDTO)
                    .toList();

        return new ConnectionDTO(
                connection.from() != null && connection.from().station() != null
                        ? connection.from().station().name()
                        : null,
                connection.to() != null && connection.to().station() != null
                        ? connection.to().station().name()
                        : null,
                connection.duration(),
                connection.products() != null ? connection.products() : List.of(),
                secciones
        );
    }

    private static SectionDTO toSectionDTO(ExternalSection section) {
        String transporte = section.journey() != null
                ? section.journey().name()
                : "A pie";

        return new SectionDTO(
                section.departure() != null ? section.departure().departure() : null,
                section.arrival() != null ? section.arrival().arrival() : null,
                section.departure() != null && section.departure().station() != null
                        ? section.departure().station().name()
                        : null,
                section.arrival() != null && section.arrival().station() != null
                        ? section.arrival().station().name()
                        : null,
                transporte
        );
    }
}