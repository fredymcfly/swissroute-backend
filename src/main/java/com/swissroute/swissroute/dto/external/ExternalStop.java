package com.swissroute.swissroute.dto.external;

public record ExternalStop(ExternalStation station,
                           String arrival,
                           Long arrivalTimestamp,
                           String departure,
                           Long departureTimestamp,
                           String platform,
                           ExternalPrognosis prognosis) {
}
