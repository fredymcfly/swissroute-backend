package com.swissroute.swissroute.service;

import com.swissroute.swissroute.dto.StationBoard;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class StationboardService {
    private final WebClient webClient;

    public StationboardService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<StationBoard>> getStationboard(String station, Integer limit, String transportType) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var ub = uriBuilder.path("/stationboard")
                            .queryParam("station", station);
                    if (limit != null) ub.queryParam("limit", limit);
                    if (transportType != null && !transportType.isBlank()) ub.queryParam("transportations", transportType);
                    return ub.build();
                })
                .accept(org.springframework.http.MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ResponseStatusException(resp.statusCode(), "Error cliente: " + body)))
                )
                .onStatus(HttpStatus::is5xxServerError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ResponseStatusException(resp.statusCode(), "Error servidor: " + body)))
                )
                .bodyToMono(StationboardResponse.class)
                .map(this::mapToDtoList);
    }

    private List<StationboardDTO> mapToDtoList(StationboardResponse response) {
        return Optional.ofNullable(response.getStationboard()).orElse(List.of()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private StationboardDTO toDto(StationboardEntry e) {
        OffsetDateTime dt = null;
        if (e.getStop() != null && e.getStop().getDeparture() != null) {
            try {
                dt = OffsetDateTime.parse(e.getStop().getDeparture());
            } catch (Exception ex) {
                // Si el formato no es ISO, podrías parsear con DateTimeFormatter personalizado aquí.
            }
        }
        return new StationboardDTO(e.getName(), e.getCategory(), e.getTo(), dt);
    }
}