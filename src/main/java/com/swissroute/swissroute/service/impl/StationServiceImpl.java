package com.swissroute.swissroute.service.impl;

import com.swissroute.swissroute.dto.external.StationApiRequest;
import com.swissroute.swissroute.dto.external.StationApiResponse;
import com.swissroute.swissroute.entity.Station;
import com.swissroute.swissroute.exception.EstacionYaExisteExcepcion;
import com.swissroute.swissroute.exception.ExternalApiException;
import com.swissroute.swissroute.mapper.StationMapper;
import com.swissroute.swissroute.repository.StationRepository;
import com.swissroute.swissroute.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final WebClient webclient;
    private  StationMapper stationMapper;


    public StationServiceImpl(StationRepository stationRepository, WebClient webclient) {
        this.stationRepository = stationRepository;
        this.webclient = webclient;
    }

    public void saveFavStation(StationApiRequest stationApiRequest, Long userId) {

        StationApiResponse stationApiResponse = webclient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/locations")
                        .queryParam("query", stationApiRequest.id())
                        .build())
                .retrieve()
                .bodyToMono(StationApiResponse.class)
                .block();

        if(stationApiResponse == null || stationApiResponse.getStations().isEmpty()) {
            throw new ExternalApiException("La API externa no devolvió estaciones");
        }

        if(stationRepository.existsByUserandStationId(userId, stationApiRequest.id())){
            throw new EstacionYaExisteExcepcion("La estación ya está registrada");
        }

        Station station = stationMapper.toEntity(stationApiRequest, userId);
        stationRepository.save(station);
    }

    @Override
    public StationApiResponse getFavStations(Long userId) {

        List<Station> stations = stationRepository.getFavStationByUserId(userId);

        List<StationApiRequest> stationRequests = stations.stream()
                .filter(s -> !s.isDeleted())
                .map(stationMapper::toDto)
                .toList();

        return new StationApiResponse(stationRequests);
    }

    @Override
    public void delete(Long id) {
        Station station = stationRepository.findById(id).orElseThrow(() -> new RuntimeException("Station not found"));
        station.setDeleted(true);
        stationRepository.save(station);
    }
}
