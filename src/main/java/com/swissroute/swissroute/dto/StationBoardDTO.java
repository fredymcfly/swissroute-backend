package com.swissroute.swissroute.dto;

import com.swissroute.swissroute.dto.external.ExternalStationBoard;

public record StationBoardDTO(String serviceName,
                              String category,
                              String to,
                              String departure) {
}


