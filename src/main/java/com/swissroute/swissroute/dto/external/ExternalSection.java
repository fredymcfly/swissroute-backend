package com.swissroute.swissroute.dto.external;

public record ExternalSection(
        ExternalJourney journey,
        ExternalStation departure,
        ExternalStation arrival
) {
}