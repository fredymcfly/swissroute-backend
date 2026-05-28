package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.FavStationDto;
import com.swissroute.swissroute.dto.StationRequest;
import com.swissroute.swissroute.dto.api.StationDto;
import com.swissroute.swissroute.dto.api.StationResponse;
import com.swissroute.swissroute.dto.api.StationResponseApi;
import com.swissroute.swissroute.entity.Station;
import com.swissroute.swissroute.entity.Usuario;
import com.swissroute.swissroute.exception.StationAlreadyExistsException;
import com.swissroute.swissroute.exception.UsuarioYaExisteException;
import com.swissroute.swissroute.repository.StationRepository;
import com.swissroute.swissroute.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {


    //private final UsuarioRepository usuarioRepository;
    private final StationRepository stationRepository;
    private final WebClient webClient;

    /*
    public StationServiceImpl(WebClient webClient, UsuarioRepository usuarioRepository, StationRepository stationRepository) {
        this.usuarioRepository = usuarioRepository;
        this.stationRepository = stationRepository;
        this.webClient = webClient;
    }

     */

    public StationServiceImpl(WebClient webClient, StationRepository stationRepository) {
        this.stationRepository = stationRepository;
        this.webClient = webClient;
    }

    public void saveFavStation(StationRequest request, Long userId) {

        StationResponseApi stationResponseApi = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/locations")
                        .queryParam("query", request.getStationName())
                        .build())
                .retrieve()
                .bodyToMono(StationResponseApi.class)
                .block();

        if(stationResponseApi == null || stationResponseApi.getStations().isEmpty()) {
            throw new StationAlreadyExistsException("La estación no existe");
        }
        if(stationRepository.existsByUserandStationId(userId, request.getStationId())) {
            throw new StationAlreadyExistsException("La estación ya está registrada");
        }

        Station station = new Station();
        station.setUserId(userId);
        station.setCreatedAt(LocalDateTime.now());
        station.setStationApiId(request.getStationId());
        station.setName(request.getStationName());
        stationRepository.save(station);
    }


    @Override
    public List<FavStationDto> getFavStations(Long userId) {

        List<Station> stations = stationRepository.getFavStationByUserId(userId);
        return stations.stream()
                .map(s -> new FavStationDto(
                        s.getId(),
                        s.getName()
                ))
                .toList();
    }

    @Override
    public void delete(Long id) {
        Station station = stationRepository.findById(id).orElseThrow(() -> new RuntimeException("Station not found"));
        station.setDeleted(true);
        stationRepository.save(station);
    }
}
