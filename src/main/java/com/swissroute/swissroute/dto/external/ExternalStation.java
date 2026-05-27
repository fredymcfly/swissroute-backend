package com.swissroute.swissroute.dto.external;

public record ExternalStation(
        ExternalStationInfo station,
        String departure,
        String arrival
) {
}