package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.FavStationDto;
import com.swissroute.swissroute.dto.StationRequest;
import com.swissroute.swissroute.dto.api.StationResponse;

import java.util.List;

public interface StationService {

    void saveFavStation(StationRequest request, Long userId);

    List<FavStationDto> getFavStations(Long userId);

    void delete(Long id);

}
