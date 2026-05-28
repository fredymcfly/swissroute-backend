package com.swissroute.swissroute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavStationDto {

    private Long stationId;
    private String stationName;
}
