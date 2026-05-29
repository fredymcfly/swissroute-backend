package com.swissroute.swissroute.mapper;

import com.swissroute.swissroute.dto.external.StationApiRequest;
import com.swissroute.swissroute.dto.external.StationApiResponse;
import com.swissroute.swissroute.entity.Station;

import java.time.LocalDateTime;

public class StationMapper {

    public StationApiRequest toDto(Station station) {
        return new StationApiRequest(station.getStationApiId(), station.getName());
    }

    public Station toEntity(StationApiRequest stationApiRequest, Long userId) {
        Station station = new Station();
        station.setUserId(userId);
        station.setCreatedAt(LocalDateTime.now());
        station.setStationApiId(stationApiRequest.id());
        station.setName(stationApiRequest.name());
        return station;
    }
}
