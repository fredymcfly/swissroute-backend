package com.swissroute.swissroute.dto.external;

public record ExternalStationBoard(ExternalStop stop,
                                   String name,
                                   String category,
                                   String number,
                                   String operator,
                                   String to) {
}
