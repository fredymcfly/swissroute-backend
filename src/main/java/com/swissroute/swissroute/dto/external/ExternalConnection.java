package com.swissroute.swissroute.dto.external;

import java.util.List;

public record ExternalConnection(
        String duration,
        ExternalStation from,
        ExternalStation to,
        List<String> products,
        List<ExternalSection> sections
) {
}