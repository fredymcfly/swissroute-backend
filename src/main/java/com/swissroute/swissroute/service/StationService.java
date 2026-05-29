package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.external.StationApiRequest;
import com.swissroute.swissroute.dto.external.StationApiResponse;

public interface StationService {
    void saveFavStation(StationApiRequest stationApiRequest, Long userId);
    StationApiResponse getFavStations(Long userId);
    void delete(Long id);

}
