package com.swissroute.swissroute.dto.external;

import java.util.List;

public record ExternalConnectionResponse(
        List<ExternalConnection> connections
) {
}